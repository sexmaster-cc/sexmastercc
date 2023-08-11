package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerTick extends ModuleListener<Jesus, TickEvent> {
    public ListenerTick(Jesus module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && module.timer.passed(600)) {
            if (module.mode.getValue() == JesusMode.SOLID) {
                if (mc.player.fallDistance > 3.0F) {
                    return;
                }

                if ((mc.player.isInLava()
                        || mc.player.isInWater())
                        && !mc.player.isSneaking()) {
                    mc.player.motionY = 0.1D;
                    return;
                }

                if (PositionUtil.inLiquid() && !mc.player.isSneaking()) {
                    mc.player.motionY = 0.1D;
                }
            }
        }
    }
}
