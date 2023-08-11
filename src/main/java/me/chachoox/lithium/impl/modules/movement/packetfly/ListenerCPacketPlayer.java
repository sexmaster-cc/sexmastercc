package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerCPacketPlayer extends ModuleListener<PacketFly, PacketEvent.Send<CPacketPlayer.Position>> {
    public ListenerCPacketPlayer(PacketFly module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.Position.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer.Position> event) {
        CPacketPlayer packet = event.getPacket();
        if (module.packets.contains(packet)) {
            module.packets.remove(packet);
            return;
        }
        event.setCanceled(true);
    }
}
