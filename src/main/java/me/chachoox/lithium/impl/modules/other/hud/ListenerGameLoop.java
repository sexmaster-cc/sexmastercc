package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerGameLoop extends ModuleListener<Hud, GameLoopEvent> {
    public ListenerGameLoop(Hud module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        long time = System.nanoTime();

        module.frames.add(time);

        while (true) {
            long f = module.frames.getFirst();
            final long ONE_SECOND = 1000000L * 1000L;
            if (time - f > ONE_SECOND) module.frames.remove();
            else break;
        }

        module.fpsCount = module.frames.size();
    }
}
