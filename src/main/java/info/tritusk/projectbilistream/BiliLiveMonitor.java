package info.tritusk.projectbilistream;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Level;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static info.tritusk.projectbilistream.BiliStreamMod.MODID;

public class BiliLiveMonitor implements Runnable {
	private String roomUrlId;
	private String roomId;
	private String cmtAddr;
	private DataOutputStream dataOutputStream;
	
	// A signal. Set to false will cause BiliLiveMonitor stop running, and thus make a thread dead.
	static volatile boolean keepRunning = true;

	private static Gson gson = new Gson();
	private static Pattern extractRoomId = Pattern.compile("ROOMID\\s=\\s(\\d+)");
	private static Pattern extractCmtAddr = Pattern.compile("<server>(.*)</server>");

	BiliLiveMonitor(String roomUrlId) {
		this.roomUrlId = roomUrlId;
	}

	private void sendHeartBeat() {
		sendSocketData(2, "");
	}

	private void sendJoinMsg() {
		long clientId = RandomUtils.nextLong(100000000000000L, 300000000000000L);
		sendSocketData(7, String.format("{\"roomid\":%s,\"uid\":%d}", roomId, clientId));
	}

	private void sendSocketData(int action, String body) {
		try {
			byte[] bodyBytes = body.getBytes("UTF-8");
			int length = bodyBytes.length + 16;
			ByteBuffer byteBuffer = ByteBuffer.allocate(length);
			byteBuffer.putInt(length);
			byteBuffer.putShort((short) 16);
			byteBuffer.putShort((short) 1);
			byteBuffer.putInt(action);
			byteBuffer.putInt(1);
			byteBuffer.put(bodyBytes);
			dataOutputStream.write(byteBuffer.array());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getRoomId() {
		try {
			URL url = new URL("http://live.bilibili.com/" + roomUrlId);
			InputStream con = url.openStream();
			String data = new String(ByteStreams.toByteArray(con), "UTF-8");
			con.close();
			Matcher matcher = extractRoomId.matcher(data);
			if (matcher.find()) {
				roomId = matcher.group(1);
			} else {
				FMLLog.log(MODID, Level.FATAL, "can not get room id");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getCmtServer() {
		try {
			URL url = new URL("http://live.bilibili.com/api/player?id=cid:" + roomId);
			InputStream con = url.openStream();
			String data = new String(ByteStreams.toByteArray(con), "UTF-8");
			con.close();
			Matcher matcher = extractCmtAddr.matcher(data);
			if (matcher.find()) {
				cmtAddr = matcher.group(1);
			} else {
				FMLLog.log(MODID, Level.FATAL, "can not get comments server address");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			getRoomId();
			getCmtServer();
			Socket socket = new Socket(cmtAddr, 788);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			InputStream inputStream = socket.getInputStream();
			sendJoinMsg();
			Timer t = new Timer();
			t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					sendHeartBeat();
					System.out.println("sent heartbeat");
				}
			}, 30000, 30000);
			while (keepRunning) {
				try {
					byte[] buf = new byte[16];
					inputStream.read(buf);
					ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
					int length = byteBuffer.getInt();
					int magic = byteBuffer.getInt();
					int action = byteBuffer.getInt();
					magic = byteBuffer.getInt();
					if (length > 16) {
						byte[] bodyByte = new byte[length - 16];
						inputStream.read(bodyByte);
						String bodyString = new String(bodyByte, "UTF-8");
						Object o = gson.fromJson(bodyString, Object.class);
						LinkedTreeMap jsonMap = (LinkedTreeMap) o; // TODO: Sometimes o is a String. Weird. Perhaps a TypeAdapter is necessary
						String msgType = (String) jsonMap.get("cmd");
						if (msgType.equals("DANMU_MSG")) {
							ArrayList infoList = (ArrayList) jsonMap.get("info");
							String danmuMsg = (String) infoList.get(1);
							String user = (String) ((ArrayList) infoList.get(2)).get(1);
							EntityPlayer player = Minecraft.getMinecraft().thePlayer;
							if (player != null) {
								player.addChatComponentMessage(new TextComponentString(
										"[Bilibili] " + user + ": " + danmuMsg));
							}

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			socket.close(); // This will be reached when monitor close signal occurs
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
