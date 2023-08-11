package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class ListenerRender2D extends ModuleListener<PollosESP, Render2DEvent> {
    public ListenerRender2D(PollosESP module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (module.pollosScreen.getValue()) {
            mc.getTextureManager().bindTexture(PollosESP.POLLOS);
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
        }
    }
}
