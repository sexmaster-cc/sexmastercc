package me.chachoox.lithium.impl.modules.movement.phase;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMove extends ModuleListener<Phase, MoveEvent> {
    public ListenerMove(Phase module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        mc.player.noClip = true;

        if (module.slow.getValue()) {
            event.setX(event.getX() * 0.3D);
            event.setZ(event.getZ() * 0.3D);
        }
    }
}
