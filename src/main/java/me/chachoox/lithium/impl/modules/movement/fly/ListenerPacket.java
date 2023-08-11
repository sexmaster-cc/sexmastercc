package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerPacket extends ModuleListener<Fly, PacketEvent.Send<CPacketPlayer>> {
    public ListenerPacket(Fly module) {
        super(module, PacketEvent.Send.class, CPacketPlayer.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayer> event) {
        ((ICPacketPlayer) event.getPacket()).setOnGround(true);
    }
}
