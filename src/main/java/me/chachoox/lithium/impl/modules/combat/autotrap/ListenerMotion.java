package me.chachoox.lithium.impl.modules.combat.autotrap;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<AutoTrap, MotionUpdateEvent> {
    public ListenerMotion(AutoTrap module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (module.isNull()) {
            module.disable();
            return;
        }

        if (mc.player.posY != module.enablePosY && module.jumpDisable.getValue()) {
            module.disable();
            return;
        }

        if (module.placeList != null) {
            module.placeList.clear();
        }

        module.target = null;
        module.getTargets();

        module.onPreEvent(module.placeList, event);
    }
}