package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerLogout extends ModuleListener<FastBreak, DisconnectEvent> {
    public ListenerLogout(FastBreak module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        module.reset();
    }
}
