package me.chachoox.lithium.impl.modules.combat.instantexp;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerLogout extends ModuleListener<InstantEXP, DisconnectEvent> {
    public ListenerLogout(InstantEXP module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        module.disable();
    }
}
