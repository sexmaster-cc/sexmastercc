package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerGameLoop extends ModuleListener<NoRender, GameLoopEvent> {
    public ListenerGameLoop(NoRender module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (mc.world != null && module.getTime() != 0) {
            mc.world.setWorldTime(-module.getTime());
        }
    }
}
