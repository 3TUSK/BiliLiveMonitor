package info.tritusk.projectbilistream;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class PlayerMonitor {
	
	private final int refreshRate;
	private int tick;
	private boolean isAvailable = false;
	
	/**
	 * Default constructor will use 10 tick (i.e. half second) as refresh rate.
	 */
	public PlayerMonitor() {
		this(10);
	}
	
	/**
	 * Construct a new PlayerMonitor object with given refresh rate, and also register into
	 * MinecraftForge's event bus.
	 * @param refreshRate The time interval between two danmakus that pop to player's chat box.
	 */
	public PlayerMonitor(int refreshRate) {
		this.refreshRate = refreshRate;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onTickingPlayer(TickEvent.PlayerTickEvent event) {
		if (isAvailable) {
			tick++;
			if (tick >= refreshRate) {
				tick = 0;
				if (DanmakuManager.INSTANCE.hasNoNewDanmaku()) {
					Danmaku danmaku = DanmakuManager.INSTANCE.pop();
					if (danmaku != null) {
						event.player.addChatComponentMessage(new TextComponentString(danmaku.getAsPlainText()));
					} 
				}
			} 
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		this.tick = 0; //Always reset timer
		this.isAvailable = true;
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		this.tick = 0; //Always reset timer
		this.isAvailable = false;
	}

}
