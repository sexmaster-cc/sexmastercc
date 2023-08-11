package me.chachoox.lithium.impl.modules.other.hud;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerReceive extends ModuleListener<Hud, PacketEvent.Receive<?>> {
    public ListenerReceive(Hud module) {
        super(module, PacketEvent.Receive.class, Integer.MAX_VALUE);
    }

    @Override
    public void call(PacketEvent.Receive<?> event) {
        module.incomingPackets += 1;
        module.lagTimer.reset();
    }
}
