package info.tritusk.projectbilistream;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class BiliLiveMonitor implements Runnable {
	
	final Socket biliDanmakuSocket;
	
	public BiliLiveMonitor() {
		// TODO: Consider putting this into separate method(s) instead of stuffing in constructor
		String danmakuServer = "";
		try {
			InputStream result = new URL("http://live.bilibili.com/api/player?id=cid:" + ModSettings.liveRoom).openConnection().getInputStream();
			byte[] bytes = new byte[2048];
			result.read(bytes);
			String rawInputAsString = "<root>" + new String(bytes, "UTF-8") + "</root>";
			InputStream wrappedInput = new ByteArrayInputStream(rawInputAsString.getBytes("UTF-8"));
			Document parsedResult = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(wrappedInput);
			danmakuServer = parsedResult.getDocumentElement().getElementsByTagName("server").item(0).getTextContent();
			biliDanmakuSocket = new Socket(danmakuServer, 81);
		} catch (Exception e) {
			throw new RuntimeException("BiliLiveMonitor cannot initialize due to error", e);
		}
	}

	@Override
	public void run() {
		try {
			DataOutputStream output = new DataOutputStream(biliDanmakuSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
