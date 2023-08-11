package me.chachoox.lithium.impl.modules.movement.noclip;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerCPlayer extends ModuleListener<NoClip, PacketEvent.Send<CPacketPlayer>> {
    public ListenerCPlayer(NoClip module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer> event) {
        if (!module.packets.remove(event.getPacket()) && module.cancelPackets.getValue()) {
            event.setCanceled(true);
        }
    }
}
