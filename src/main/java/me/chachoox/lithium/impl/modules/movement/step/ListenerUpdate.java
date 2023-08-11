package me.chachoox.lithium.impl.modules.movement.step;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerUpdate extends ModuleListener<Step, UpdateEvent> {
    public ListenerUpdate(Step module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.timer.passed(200)) {
            mc.player.stepHeight = module.height.getValue();
        }
        else {
            mc.player.stepHeight = 0.6f;
        }
    }
}
