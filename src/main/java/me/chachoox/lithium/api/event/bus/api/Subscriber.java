package me.chachoox.lithium.api.event.bus.api;

import me.chachoox.lithium.api.event.bus.Listener;

import java.util.Collection;

public interface Subscriber {
    Collection<Listener<?>> getListeners();
}

