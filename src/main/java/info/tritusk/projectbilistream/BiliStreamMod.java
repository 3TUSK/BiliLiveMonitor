package info.tritusk.projectbilistream;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BiliStreamMod.MODID, name = BiliStreamMod.NAME, version = "0.0.0.0", clientSideOnly = true)
public enum BiliStreamMod {
	
	INSTANCE;
	
	@Mod.InstanceFactory
	public BiliStreamMod getInstance() {
		return INSTANCE;
	}
	
	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent event) {
		new PlayerMonitor();
	}
	
	public static final String MODID = "bilistreammod";
	public static final String NAME = "BiliStreamMod";

}
