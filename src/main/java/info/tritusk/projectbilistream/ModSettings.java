package info.tritusk.projectbilistream;

import net.minecraftforge.common.config.Config;

@Config(modid = BiliStreamMod.MODID)
public final class ModSettings {
	
	@Config.Comment("The live room number. Self-explained.")
	@Config.LangKey("config.bilistream.liveroom")
	@Config.RangeInt(min = 0)
	public static int liveRoom = 0;
	
	@Config.Comment("The Bilibili API token.")
	@Config.LangKey("config.bilistream.token")
	public static String token = "";

}
