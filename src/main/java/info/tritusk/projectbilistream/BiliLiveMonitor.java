package info.tritusk.projectbilistream;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.RandomUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SideOnly(Side.CLIENT)
public class BiliLiveMonitor implements Runnable {
	private DataOutputStream dataOutputStream;

	private static Gson gson = new Gson();

	private void sendHeartBeat() {
		sendSocketData(2, "");
	}

	private void sendJoinMsg() {
		long clientId = RandomUtils.nextLong(100000000000000L, 300000000000000L);
		sendSocketData(7, String.format("{\"roomid\":%s,\"uid\":%d}", ModSettings.liveRoom, clientId));
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

	@Override
	public void run() {
		try {
			InputStream result = new URL("http://live.bilibili.com/api/player?id=cid:" + ModSettings.liveRoom).openConnection().getInputStream();
			result.available(); //This is magic: without this call, result.read cannot give full result?!
			byte[] bytes = new byte[2048];
			result.read(bytes);
			String rawInputAsString = "<root>" + new String(bytes, "UTF-8") + "</root>";
			rawInputAsString = rawInputAsString.replaceAll("[\u0000\u0014]", ""); //Get rid of invalid xml char. Not sure about the origin. Remove this replaceAll call when it is unnecessary.
			InputStream wrappedInput = new ByteArrayInputStream(rawInputAsString.getBytes("UTF-8"));
			Document parsedResult = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wrappedInput);
			String danmakuServer = parsedResult.getDocumentElement().getElementsByTagName("server").item(0).getTextContent();
			Socket socket = new Socket(danmakuServer, 788);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
//			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
			while (true) {
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
						LinkedTreeMap jsonMap = (LinkedTreeMap) o;
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

//							System.out.println(user + ": " + danmuMsg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

}
