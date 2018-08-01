package info.tritusk.projectbilistream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Charlie Jiang
 * @since rv1
 */
public class Overlay {
    public static volatile boolean show = false;
    public static volatile int count = -1;

    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
    {
        if (!show) return;
        //render everything onto the screen
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if ((mc.inGameHasFocus || (mc.currentScreen != null && (mc.currentScreen instanceof GuiChat)))) {
                ScaledResolution res = new ScaledResolution(mc);
                FontRenderer fontRender = mc.fontRenderer;
                int width = res.getScaledWidth();
                int height = res.getScaledHeight();

                String test = "观看人数：" + count;
                int x = width - 100;
                int y = height - fontRender.FONT_HEIGHT;
                int color = 0xffffff;
                mc.fontRenderer.drawStringWithShadow(test, x, y, color);
            }
        }
    }
}
