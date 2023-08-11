package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerRender extends ModuleListener<PvPInfo, Render2DEvent> {
    public ListenerRender(PvPInfo module) {
        super(module, Render2DEvent.class);
    }

    @Override
    public void call(Render2DEvent event) {
        if (mc.gameSettings.showDebugInfo) {
            return;
        }

        module.onRender(event.getResolution());
    }
}
