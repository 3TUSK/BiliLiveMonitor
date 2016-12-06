package info.tritusk.projectbilistream;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@SideOnly(Side.CLIENT)
	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent event) {
		new PlayerMonitor();
//		Thread t = new Thread(new BiliLiveMonitor());
//		t.start();
	}


}
