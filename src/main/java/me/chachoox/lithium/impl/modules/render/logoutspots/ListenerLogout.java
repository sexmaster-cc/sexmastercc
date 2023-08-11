package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerLogout extends ModuleListener<LogoutSpots, DisconnectEvent> {
    public ListenerLogout(LogoutSpots module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        module.spots.clear();
    }
}
