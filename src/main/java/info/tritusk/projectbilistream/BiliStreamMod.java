package info.tritusk.projectbilistream;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BiliStreamMod.MODID, name = BiliStreamMod.NAME, version = "0.0.0.0", clientSideOnly = true)
public class BiliStreamMod {
	static final String MODID = "bilistreammod";
	static final String NAME = "BiliStreamMod";
	static Thread t;
	@Mod.Instance(MODID)
	private static BiliStreamMod INSTANCE;

	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent event) {
		new PlayerMonitor();
		ClientCommandHandler.instance.registerCommand(new Command());

		t = new Thread(new BiliLiveMonitor(Integer.toString(ModSettings.liveRoom)));
		t.start();
	}

}
