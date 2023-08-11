package me.chachoox.lithium.impl.modules.misc.pingspoof;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketKeepAlive;

public class ListenerKeepAlive extends ModuleListener<PingSpoof, PacketEvent.Send<CPacketKeepAlive>> {
    public ListenerKeepAlive(PingSpoof module) {
        super(module, PacketEvent.Send.class, CPacketKeepAlive.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketKeepAlive> event) {
        if (!mc.isSingleplayer()) {
            module.onPacket(event.getPacket());
            event.setCanceled(true);
        }
    }
}