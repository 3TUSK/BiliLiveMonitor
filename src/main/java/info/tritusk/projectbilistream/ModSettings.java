package info.tritusk.projectbilistream;

import net.minecraftforge.common.config.Config;

@Config(modid = BiliStreamMod.MODID)
public final class ModSettings {
	
	@Config.Comment("The live room number. Self-explained.")
	@Config.LangKey("config.bilistream.liveroom")
	@Config.RangeInt(min = 0)
	public static int liveRoom = 0;

}
