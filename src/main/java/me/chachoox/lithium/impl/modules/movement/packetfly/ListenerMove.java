package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;

public class ListenerMove extends ModuleListener<PacketFly, MoveEvent> {
    public ListenerMove(PacketFly module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (!event.isCanceled()) {
            if (module.mode.getValue() != PacketFlyMode.SETBACK && module.lastTpID == 0) {
                return;
            }
            event.setCanceled(true);
            event.setX(mc.player.motionX);
            event.setY(module.conceal.getValue() ? module.concealOffset : mc.player.motionY);
            event.setZ(mc.player.motionZ);
            if (module.phase.getValue() != PhaseMode.OFF && (module.phase.getValue() == PhaseMode.SEMI || module.checkHitBox())) {
                mc.player.noClip = true;
            }
        }
    }
}
