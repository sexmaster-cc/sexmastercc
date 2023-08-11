package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.impl.event.events.movement.liquid.LiquidJumpEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerLiquidJump extends ModuleListener<Jesus, LiquidJumpEvent> {
    public ListenerLiquidJump(Jesus module) {
        super(module, LiquidJumpEvent.class);
    }

    @Override
    public void call(LiquidJumpEvent event) {
        if (mc.player != null && mc.player.equals(event.getEntity()) && (mc.player.isInWater() || mc.player.isInLava()) && (mc.player.motionY == 0.1 || mc.player.motionY == 0.5)) {
            event.setCanceled(true);
        }
    }
}
