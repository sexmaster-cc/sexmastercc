package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ListenerSwap extends ModuleListener<FastBreak, PacketEvent.Send<CPacketHeldItemChange>> {
    public ListenerSwap(FastBreak module) {
        super(module, PacketEvent.Send.class, CPacketHeldItemChange.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketHeldItemChange> event) {
        CPacketHeldItemChange packet = event.getPacket();
        if (module.pos != null) {
            if (Managers.SWITCH.getSlot() == packet.getSlotId()) {
                return;
            }

            if (module.swap.getValue() == Swap.ALTERNATIVE) {
                module.softReset(true);
            }
        }
    }
}
