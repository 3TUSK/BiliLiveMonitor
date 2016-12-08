package info.tritusk.projectbilistream;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BiliStreamMod.MODID, name = BiliStreamMod.NAME, version = "0.0.0.0", clientSideOnly = true)
public class BiliStreamMod {
	static final String MODID = "bilistreammod";
	static final String NAME = "BiliStreamMod";

	@Mod.Instance(MODID)
	private static BiliStreamMod INSTANCE;

	@Mod.InstanceFactory
	public BiliStreamMod getInstance() {
		return INSTANCE;
	}

	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent event) {
		new PlayerMonitor();
		Thread t = new Thread(new BiliLiveMonitor());
		t.start();
	}

}
