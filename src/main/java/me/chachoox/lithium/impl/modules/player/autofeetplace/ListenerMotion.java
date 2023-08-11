package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<AutoFeetPlace, MotionUpdateEvent> {
    public ListenerMotion(AutoFeetPlace module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if ((mc.player.posY != module.enablePosY || !mc.player.onGround) && module.jumpDisable.getValue()) {
            module.disable();
            return;
        }

        module.onPreEvent(module.getPlacements(), event);
    }
}
