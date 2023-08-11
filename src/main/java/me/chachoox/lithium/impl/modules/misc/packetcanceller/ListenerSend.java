package me.chachoox.lithium.impl.modules.misc.packetcanceller;

import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

public class ListenerSend extends ModuleListener<PacketCanceller, PacketEvent.Send<?>> {
    public ListenerSend(PacketCanceller module) {
        super(module, PacketEvent.Send.class, Integer.MAX_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof CPacketPlayerTryUseItemOnBlock && module.cTryUseItemOnBlock.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketPlayerDigging && module.cDigging.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketInput && module.cInput.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketPlayer && module.cPlayer.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketEntityAction && module.cEntityAction.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketUseEntity && module.cUseEntity.getValue()) {
            cancel(event);
        } else if (packet instanceof CPacketVehicleMove && module.cVehicleMove.getValue()) {
            cancel(event);
        }
    }

    private void cancel(Event event) {
        module.packets++;
        event.setCanceled(true);
    }
}