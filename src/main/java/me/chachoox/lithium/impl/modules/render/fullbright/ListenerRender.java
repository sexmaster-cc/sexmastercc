package me.chachoox.lithium.impl.modules.render.fullbright;

import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerRender extends ModuleListener<Fullbright, Render2DEvent> {
    public ListenerRender(Fullbright module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (module.bozeMode.getValue()) {
            Render2DUtil.drawRect(0, 0, mc.displayWidth, mc.displayHeight, module.getColor().getRGB());
        }
    }
}
