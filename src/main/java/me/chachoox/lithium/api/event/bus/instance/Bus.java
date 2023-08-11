package me.chachoox.lithium.api.event.bus.instance;

import me.chachoox.lithium.api.event.bus.SimpleBus;
import me.chachoox.lithium.api.event.bus.api.EventBus;

public class Bus {
    public static final EventBus EVENT_BUS = new SimpleBus();
}
