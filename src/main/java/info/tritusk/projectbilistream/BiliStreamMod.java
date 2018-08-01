package info.tritusk.projectbilistream;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BiliStreamMod.MODID, name = BiliStreamMod.NAME, version = "0.0.0.0", clientSideOnly = true)
public class BiliStreamMod {
	static final String MODID = "bilistreammod";
	static final String NAME = "BiliStreamMod";
	static Thread t;
	@Mod.Instance(MODID)
	public static BiliStreamMod INSTANCE;

	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent event) {
		//new PlayerMonitor(); //Because that receives TickEvent. Should be off for now so that we could save cpu a bit
		ClientCommandHandler.instance.registerCommand(new Command());
		MinecraftForge.EVENT_BUS.register(new Overlay());

		if (ModSettings.liveRoom != 0) {
			t = new Thread(new BiliLiveMonitor(Integer.toString(ModSettings.liveRoom)));
			t.start();
		}
	}
}
