package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerUpdate extends ModuleListener<NoClip, UpdateEvent> {
    public ListenerUpdate(NoClip module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed = (mc.player.movementInput.sneak && module.shift.getValue()) ? -0.062 : 0.0;
        double[] strafing = module.getMotion();
        for (int i = 1; i < 1 + 1; ++i) {
            mc.player.motionX = strafing[0] * (double) i * module.phaseSpeed.getValue();
            mc.player.motionY = speed * (double) i;
            mc.player.motionZ = strafing[1] * (double) i * module.phaseSpeed.getValue();
            module.sendPackets(mc.player.motionX, mc.player.motionY, mc.player.motionZ);
        }
    }
}
