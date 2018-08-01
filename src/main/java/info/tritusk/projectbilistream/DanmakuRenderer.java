package info.tritusk.projectbilistream;

import charlie.bililivelib.danmaku.datamodel.Danmaku;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DanmakuRenderer {
    private static Style biliStyle = new Style();
    private static Style userStyle = new Style();

    static {
        biliStyle.setColor(TextFormatting.AQUA);
        userStyle.setColor(TextFormatting.GOLD);
    }

    public static ITextComponent renderDanmaku(Danmaku danmaku) {
        ITextComponent c1 = new TextComponentString("[");
        ITextComponent c2 = new TextComponentString("Bilibili Live");
        ITextComponent c3 = new TextComponentString("] ");
        ITextComponent c4 = new TextComponentString(danmaku.getUser().getName() + " > ");
        ITextComponent c5 = new TextComponentString(danmaku.getContent());

        c2.setStyle(biliStyle);
        c4.setStyle(userStyle);
        c1.appendSibling(c2);
        c1.appendSibling(c3);
        c1.appendSibling(c4);
        c1.appendSibling(c5);
        return c1;
    }
}
