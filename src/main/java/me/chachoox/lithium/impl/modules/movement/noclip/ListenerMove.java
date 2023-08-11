package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMove extends ModuleListener<NoClip, MoveEvent> {
    public ListenerMove(NoClip module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (module.adjustMotion.getValue()) {
            event.setX(mc.player.motionX);
            event.setY(mc.player.motionY);
            event.setZ(mc.player.motionZ);
            if (module.removeHitbox.getValue() && module.checkHitBoxes()) {
                mc.player.noClip = true;
            }
        }
    }
}
