package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerLogout extends ModuleListener<Announcer, DisconnectEvent> {
    public ListenerLogout(Announcer module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        module.reset();
    }
}
