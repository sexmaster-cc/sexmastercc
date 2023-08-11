package me.chachoox.lithium.impl.modules.combat.instantweb;

import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<InstantWeb, MotionUpdateEvent> {
    public ListenerMotion(InstantWeb module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (mc.player.posY != module.enablePosY && module.jumpDisable.getValue()) {
            module.disable();
            return;
        }

        module.target = CombatUtil.getTarget(module.targetRange.getValue());
        if (module.target == null) {
            return;
        }

        module.onPreEvent(module.getPlacements(), event);
    }
}
