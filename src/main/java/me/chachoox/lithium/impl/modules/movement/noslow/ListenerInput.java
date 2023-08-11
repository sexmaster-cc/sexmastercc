package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.movement.InputUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerInput extends ModuleListener<NoSlow, InputUpdateEvent> {
    public ListenerInput(NoSlow module) {
        super(module, InputUpdateEvent.class);
    }

    @Override
    public void call(InputUpdateEvent event) {
        if (module.items.getValue()) {
            if (mc.player.isHandActive() && !mc.player.isRiding()) {
                mc.player.movementInput.moveForward /= 0.2f;
                mc.player.movementInput.moveStrafe /= 0.2f;
            }
        }
    }
}
