package me.chachoox.lithium.impl.modules.movement.holepull;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;

public class ListenerUpdate extends ModuleListener<HolePull, UpdateEvent> {
    public ListenerUpdate(HolePull module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (mc.player.isSpectator()) {
            return;
        }
        if (module.mode.getValue() == PullMode.SNAP && module.timer.getValue()) {
            if (module.boosted >= module.timerLength.getValue()) {
                Managers.TIMER.reset();
                return;
            }
            Managers.TIMER.set(module.timerAmount.getValue());
            ++module.boosted;
        }
    }
}
