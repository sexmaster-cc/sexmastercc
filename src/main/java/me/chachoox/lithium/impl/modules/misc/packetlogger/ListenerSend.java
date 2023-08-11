package me.chachoox.lithium.impl.modules.misc.packetlogger;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketCloseWindow;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketConfirmTransaction;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.*;
import net.minecraft.util.StringUtils;

public class ListenerSend extends ModuleListener<PacketLogger, PacketEvent.Send<?>> {
    public ListenerSend(PacketLogger module) {
        super(module, PacketEvent.Send.class, Integer.MIN_VALUE);
    }

    @Override
    public void call(PacketEvent.Send<?> event) {
        if (module.packets.getValue() == Packets.INCOMING) {
            return;
        }

        String packetInfo = null;
        
        module.initializeWriter();

        if (event.getPacket() instanceof CPacketPlayer && module.cPlayer.getValue()) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packetInfo = "CPacketPlayer:"
                    + "\n- X: " + ((ICPacketPlayer) packet).getX()
                    + "\n- Y: " + ((ICPacketPlayer) packet).getY()
                    + "\n- Z: " + ((ICPacketPlayer) packet).getZ()
                    + "\n- OnGround: " + packet.isOnGround()
                    + "\n- Pitch: " + ((ICPacketPlayer) packet).getPitch()
                    + "\n- Yaw: " + ((ICPacketPlayer) packet).getYaw();
        } else if (event.getPacket() instanceof CPacketUseEntity && module.cUseEntity.getValue()) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            packetInfo = "CPacketUseEntity:"
                    + "\n- Hand: " + (packet.getHand() == null ? "null" : packet.getHand().name())
                    + "\n- HitVec: " + packet.getHitVec();
            Entity entity = packet.getEntityFromWorld(mc.world);
            if (entity != null) {
                packetInfo = packetInfo
                        + "\n- EntityName: " + entity.getName()
                        + "\n- EntityID: " + entity.getEntityId();
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && module.cTryUseItemOnBlock.getValue()) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
            packetInfo = "CPacketPlayerTryUseItemOnBlock:"
                    + "\n- Pos: " + packet.getPos()
                    + "\n- Direction: " + packet.getDirection().getName()
                    + "\n- FacingX: " + packet.getFacingX()
                    + "\n- FacingY: " + packet.getFacingY()
                    + "\n- FacingZ: " + packet.getFacingZ()
                    + "\n- Hand: " + packet.getHand().name();
        } else if (event.getPacket() instanceof CPacketEntityAction && module.cEntityAction.getValue()) {
            CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
            packetInfo = "CPacketEntityAction:"
                    + "\n- Action: " + packet.getAction().name()
                    + "\n- AuxData: " + packet.getAuxData();
        } else if (event.getPacket() instanceof CPacketPlayerDigging && module.cDigging.getValue()) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
            packetInfo = "CPacketPlayerDigging:"
                    + "\n- Action: " + packet.getAction().name()
                    + "\n- Pos: " + packet.getPosition()
                    + "\n- Facing: " + packet.getFacing();
        } else if (event.getPacket() instanceof CPacketCloseWindow && module.cCloseWindow.getValue()) {
            CPacketCloseWindow packet = (CPacketCloseWindow) event.getPacket();
            packetInfo = "CPacketCloseWindow:"
                    + "\n- WindowID: " + ((ICPacketCloseWindow) packet).getWindowId();
        } else if (event.getPacket() instanceof CPacketClickWindow && module.cClickWindow.getValue()) {
            CPacketClickWindow packet = (CPacketClickWindow) event.getPacket();
            packetInfo = "CPacketClickWindow:"
                    + "\n- WindowID: " + packet.getWindowId()
                    + "\n- SlotID: " + packet.getSlotId()
                    + "\n- UsedButton: " + packet.getUsedButton()
                    + "\n- ActionNumber: " + packet.getActionNumber()
                    + "\n- ItemName: " + packet.getClickedItem().getDisplayName()
                    + "\n- ClickType: " + packet.getClickType().name();
        } else if (event.getPacket() instanceof CPacketHeldItemChange && module.cHeldItem.getValue()) {
            CPacketHeldItemChange packet = (CPacketHeldItemChange) event.getPacket();
            packetInfo = "CPacketHeldItemChange:"
                    + "\n- SlotID: " + packet.getSlotId();
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && module.cUseItem.getValue()) {
            CPacketPlayerTryUseItem packet = (CPacketPlayerTryUseItem) event.getPacket();
            packetInfo = "CPacketPlayerTryUseItem:"
                    + "\n- Hand: " + packet.getHand();
        } else if (event.getPacket() instanceof CPacketAnimation && module.cAnimation.getValue()) {
            CPacketAnimation packet = (CPacketAnimation) event.getPacket();
            packetInfo = "CPacketAnimation:"
                    + "\n- Hand: " + packet.getHand();
        } else if (event.getPacket() instanceof CPacketConfirmTransaction && module.cConfirmTransaction.getValue()) {
            CPacketConfirmTransaction packet = (CPacketConfirmTransaction) event.getPacket();
            packetInfo = "CPacketConfirmTransaction"
                    + "\n- WindowID: " + packet.getWindowId()
                    + "\n- UID: " + packet.getUid()
                    + "\n- Accepted: " + ((ICPacketConfirmTransaction) packet).getAccepted();
        } else if (event.getPacket() instanceof CPacketConfirmTeleport && module.cConfirmTeleport.getValue()) {
            CPacketConfirmTeleport packet = (CPacketConfirmTeleport) event.getPacket();
            packetInfo = "CPacketConfirmTeleport:"
                    + "\n- TeleportID: " + packet.getTeleportId();
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
