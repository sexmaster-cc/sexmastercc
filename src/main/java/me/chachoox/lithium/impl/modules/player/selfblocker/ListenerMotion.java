package me.chachoox.lithium.impl.modules.player.selfblocker;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<SelfBlocker, MotionUpdateEvent> {
    public ListenerMotion(SelfBlocker module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (mc.player.posY != module.enablePosY && module.jumpDisable.getValue()) {
            module.disable();
            return;
        }

        module.onPreEvent(module.getPlacements(), event);
    }
}
