package me.chachoox.lithium.impl.modules.player.fastplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBoat;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public class ListenerPlaceBoat extends ModuleListener<FastPlace, PacketEvent.Send<CPacketPlayerTryUseItemOnBlock>> {
    public ListenerPlaceBoat(FastPlace module) {
        super(module, PacketEvent.Send.class, CPacketPlayerTryUseItemOnBlock.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayerTryUseItemOnBlock> event) {
        if (module.boatFix.getValue()) {
            CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
            if (mc.player.getHeldItem(packet.getHand()).getItem() instanceof ItemBoat) {
                event.setCanceled(true);
            }
        }
    }
}

