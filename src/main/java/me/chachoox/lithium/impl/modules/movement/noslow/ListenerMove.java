package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;

public class ListenerMove extends ModuleListener<NoSlow, MoveEvent> {
    public ListenerMove(NoSlow module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (module.doWeb()) {
            switch (module.antiWeb.getValue()) {
                case MOTION: {
                    double[] calc = MovementUtil.directionSpeed(((double) module.speed.getValue()) / 10);
                    event.setX(calc[0]);
                    event.setZ(calc[1]);
                    event.setY(event.getY() - (module.speed.getValue() / 10));
                    break;
                }
                case TIMER: {
                    if (!mc.player.onGround) {
                        Managers.TIMER.set(module.speed.getValue());
                        module.timerCheck = true;
                        break;
                    }
                }
            }
        }
    }
}
