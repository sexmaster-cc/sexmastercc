package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<AutoCrystal, MotionUpdateEvent> {
    public ListenerMotion(AutoCrystal module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            module.doAutoCrystal();
        }
    }
}