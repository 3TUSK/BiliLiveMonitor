package info.tritusk.projectbilistream;

import java.io.IOException;
import java.net.Socket;

public class BiliLiveMonitor implements Runnable {
	
	final Socket biliDanmakuSocket;
	
	public BiliLiveMonitor() {
		try {
			biliDanmakuSocket = new Socket("http://livecmt.bilibili.com/", 81);
		} catch (IOException e) {
			throw new IllegalStateException("BiliLiveMonitor cannot initialize due to error", e);
		}
	}

	@Override
	public void run() {
		try {
			biliDanmakuSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
