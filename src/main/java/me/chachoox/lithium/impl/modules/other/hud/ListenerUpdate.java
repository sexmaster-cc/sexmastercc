package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerUpdate extends ModuleListener<Hud, UpdateEvent> {
    public ListenerUpdate(Hud module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.timer.passed(1000L)) {
            module.outgoingPackets = 0;
            module.incomingPackets = 0;
            module.timer.reset();
        }
    }
}
