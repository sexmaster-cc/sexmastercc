package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<Speed, MotionUpdateEvent> {
    public ListenerMotion(Speed module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (!MovementUtil.anyMovementKeys()) {
            MovementUtil.setMotion(0 , mc.player.motionY, 0);
        }
        module.distance = MovementUtil.getDistance2D();
    }
}
