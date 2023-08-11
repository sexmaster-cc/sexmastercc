package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketPlayerPosLook;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;

public class ListenerPosLook extends ModuleListener<PacketFly, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(PacketFly module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        if (mc.player != null) {
            SPacketPlayerPosLook packet = event.getPacket();
            if (mc.player.isEntityAlive()
                    && mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), false)
                    && !(mc.currentScreen instanceof GuiDownloadTerrain)
                    && module.mode.getValue() != PacketFlyMode.SETBACK) {
                module.teleportMap.remove(packet.getTeleportId());
            }
            ((ISPacketPlayerPosLook)packet).setYaw(mc.player.rotationYaw);
            ((ISPacketPlayerPosLook)packet).setYaw(mc.player.rotationPitch);
            module.lagTime = 10;
            module.lastTpID = packet.getTeleportId();
        }
    }
}
