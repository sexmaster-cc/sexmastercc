package me.chachoox.lithium.impl.modules.misc.packetlogger;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketCloseWindow;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.network.play.server.*;
import net.minecraft.util.StringUtils;

public class ListenerReceive extends ModuleListener<PacketLogger, PacketEvent.Receive<?>> {
    public ListenerReceive(PacketLogger module) {
        super(module, PacketEvent.Receive.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Receive<?> event) {
        if (module.packets.getValue() == Packets.OUTGOING) {
            return;
        }

        module.initializeWriter();

        String packetInfo = null;

        if (event.getPacket() instanceof SPacketPlayerPosLook && module.sPlayerPosLook.getValue()) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packetInfo = "SPacketPlayerPosLook:"
                    + "\n- Pitch: " + packet.getPitch()
                    + "\n- Yaw: " + packet.getYaw()
                    + "\n- TeleportID: " + packet.getTeleportId();
        } else if (event.getPacket() instanceof SPacketPlayerListItem && module.sPlayerListItem.getValue()) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem) event.getPacket();
            packetInfo = "SPacketPlayerListItem:"
                    + "\n- Action: " + packet.getAction().name()
                    + "\n- Entries: " + packet.getEntries();
        } else if (event.getPacket() instanceof SPacketOpenWindow && module.sOpenWindow.getValue()) {
            SPacketOpenWindow packet = (SPacketOpenWindow) event.getPacket();
            packetInfo = "SPacketOpenWindow:"
                    + "\n- WindowID: " + packet.getWindowId()
                    + "\n- EntityID: " + packet.getEntityId()
                    + "\n- WindowTitle: " + packet.getWindowTitle().getUnformattedText()
                    + "\n- GuiID: " + packet.getGuiId()
                    + "\n- SlotCount: " + packet.getSlotCount();
        } else if (event.getPacket() instanceof SPacketCloseWindow && module.sCloseWindow.getValue()) {
            SPacketCloseWindow packet = (SPacketCloseWindow) event.getPacket();
            packetInfo = "SPacketCloseWindow:"
                    + "\n- WindowID: " + ((ISPacketCloseWindow) packet).getWindowID();
        } else if (event.getPacket() instanceof SPacketSetSlot && module.sSetSlot.getValue()) {
            SPacketSetSlot packet = (SPacketSetSlot) event.getPacket();
            packetInfo = "SPacketSetSlot:"
                    + "\n- WindowID: " + packet.getWindowId()
                    + "\n- SlotID: " + packet.getSlot()
                    + "\n- ItemName: " + packet.getStack().getDisplayName();
        } else if (event.getPacket() instanceof SPacketEntityStatus && module.sEntityStatus.getValue()) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            packetInfo = "SPacketEntityStatus:"
                    + "\n- EntityId: " + packet.getEntity(mc.world).getEntityId()
                    + "\n- EntityName: " + packet.getEntity(mc.world).getName()
                    + "\n- OpCode: " + packet.getOpCode();
        } else if (event.getPacket() instanceof SPacketResourcePackSend && module.sPacketResource.getValue()) {
            SPacketResourcePackSend packet = (SPacketResourcePackSend) event.getPacket();
            packetInfo = "SPacketResourcePackSend:"
                    + "\n- URL: " + packet.getURL()
                    + "\n- Hash: " + packet.getHash();
        } else if (event.getPacket() instanceof SPacketConfirmTransaction && module.sConfirmTransaction.getValue()) {
            SPacketConfirmTransaction packet = (SPacketConfirmTransaction) event.getPacket();
            packetInfo = "SPacketConfirmTransaction:"
                    + "\n- WindowID: " + packet.getWindowId()
                    + "\n- ActionNumber: " + packet.getActionNumber()
                    + "\n- Accepted: " + packet.wasAccepted();
        }

        if (StringUtils.isNullOrEmpty(packetInfo)) {
            return;
        }

        if (module.showCanceled.getValue()) {
            packetInfo = packetInfo + "\n- Canceled: " + event.isCanceled();
        }

        module.log(packetInfo);
        module.write(packetInfo);
    }
}
