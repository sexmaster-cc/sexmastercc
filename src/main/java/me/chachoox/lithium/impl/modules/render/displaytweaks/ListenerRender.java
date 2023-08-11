package me.chachoox.lithium.impl.modules.render.displaytweaks;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.client.gui.ScaledResolution;

public class ListenerRender extends ModuleListener<DisplayTweaks, Render2DEvent> {
    public ListenerRender(DisplayTweaks module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        final ScaledResolution res = event.getResolution();
        if (module.hotbarKeys.getValue() && !mc.player.isSpectator()) {
            int x = res.getScaledWidth() / 2 - 87;
            int y = res.getScaledHeight() - 18;
            int length = mc.gameSettings.keyBindsHotbar.length;
            for (int i = 0; i < length; i++) {
                mc.fontRenderer.drawStringWithShadow(mc.gameSettings.keyBindsHotbar[i].getDisplayName(), x + i * 20, y, module.getColor().getRGB());
            }
        }
    }
}
