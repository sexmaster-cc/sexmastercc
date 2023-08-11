package me.chachoox.lithium.impl.modules.movement.inventorymove;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ListenerClick extends ModuleListener<InventoryMove, PacketEvent.Send<CPacketClickWindow>> {
    public ListenerClick(InventoryMove module) {
        super(module, PacketEvent.Send.class, CPacketClickWindow.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketClickWindow> event) {
        if (Managers.ACTION.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (Managers.ACTION.isSprinting()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }
}
