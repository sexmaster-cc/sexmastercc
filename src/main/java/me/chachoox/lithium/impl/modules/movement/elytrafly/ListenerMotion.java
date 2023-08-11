package me.chachoox.lithium.impl.modules.movement.elytrafly;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.util.math.MathHelper;

public class ListenerMotion extends ModuleListener<ElytraFly, MotionUpdateEvent> {
    public ListenerMotion(ElytraFly module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (module.wasp.getValue() && module.boost.getValue()) {
            module.boost.setValue(false);
            module.wasp.setValue(false);
        }
        if (event.getStage() == Stage.PRE && module.isElytra() && !module.boost.getValue()) {
            float moveStrafe = mc.player.movementInput.moveStrafe;
            float moveForward = mc.player.movementInput.moveForward;
            float strafe = moveStrafe * 90 * (moveForward != 0 ? 0.5f : 1);
            event.setYaw(MathHelper.wrapDegrees(mc.player.rotationYaw - strafe - (moveForward < 0 ? 180 : 0)));
        }
    }
}
