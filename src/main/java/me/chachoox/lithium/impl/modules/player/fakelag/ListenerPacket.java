package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.*;

public class ListenerPacket extends ModuleListener<FakeLag, PacketEvent.Send<?>> {
    public ListenerPacket(FakeLag module) {
        super(module, PacketEvent.Send.class);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (!(event.getPacket() instanceof CPacketChatMessage
                || event.getPacket() instanceof CPacketConfirmTeleport
                || event.getPacket() instanceof CPacketKeepAlive
                || event.getPacket() instanceof CPacketTabComplete
                || event.getPacket() instanceof CPacketClientStatus)) {
            if (!module.cache.contains(event.getPacket())) {
                module.cache.add(event.getPacket());
                event.setCanceled(true);
            }
        }
    }
}
