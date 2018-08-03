package info.tritusk.projectbilistream;

import charlie.bililivelib.danmaku.DanmakuReceiver;
import charlie.bililivelib.danmaku.datamodel.Danmaku;
import charlie.bililivelib.danmaku.dispatch.DanmakuDispatcher;
import charlie.bililivelib.danmaku.event.DanmakuAdapter;
import charlie.bililivelib.danmaku.event.DanmakuEvent;
import charlie.bililivelib.room.Room;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.NotNull;

public class BiliLiveMonitor extends DanmakuAdapter implements Runnable {
	private String roomUrlId;
	private String roomId;
	private String cmtAddr;
	private static DanmakuReceiver receiver;

	private static Gson gson = new Gson();

	BiliLiveMonitor(String roomUrlId) {
		this.roomUrlId = roomUrlId;
	}

	@Override
	public void run() {
		Overlay.show = false;
		try {
			if (receiver != null) receiver.disconnect();
		} catch (Exception ex) { /* Just ignore that */ }
		try {
			receiver = new DanmakuReceiver(Room.getRealRoomID(Integer.parseInt(roomUrlId)));
			receiver.getDispatchManager().registerDispatcher(new DanmakuDispatcher());
			receiver.addDanmakuListener(this);
			receiver.connect();
			ModSettings.liveRoom = Integer.parseInt(roomUrlId);
		} catch (Exception e) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[Bilibili Live] 连接失败。" + e));
		}
	}

	@Override
	public void danmakuEvent(@NotNull DanmakuEvent event) {
		Minecraft.getMinecraft().player.sendMessage(DanmakuRenderer.renderDanmaku((Danmaku) event.getParam()));
	}

	@Override
	public void watcherCountEvent(@NotNull DanmakuEvent event) {
		Overlay.count = (Integer) event.getParam();
	}

	@Override
	public void errorEvent(@NotNull DanmakuEvent event) {
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[Bilibili Live] 弹幕掉线！！！！！" + event.getParam()));
	}

	@Override
	public void statusEvent(@NotNull DanmakuEvent event) {
		if (event.getKind() == DanmakuEvent.Kind.JOINED) {
			Overlay.show = true;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[Bilibili Live] 安排上了。"));
		} else if (event.getKind() == DanmakuEvent.Kind.ERROR_DOWN) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[Bilibili Live] 弹幕掉线！！！！！" + event.getParam()));
		}
	}
}
