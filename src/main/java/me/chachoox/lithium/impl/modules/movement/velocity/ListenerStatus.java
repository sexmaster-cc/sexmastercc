package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.Objects;

public class ListenerStatus extends ModuleListener<Velocity, PacketEvent.Receive<SPacketEntityStatus>> {
    public ListenerStatus(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketEntityStatus.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketEntityStatus> event) {
        SPacketEntityStatus packet = event.getPacket();
        if (module.fishingRod.getValue() && packet.getOpCode() == 31 && !event.isCanceled()) {
            if (packet.getEntity(mc.world) instanceof EntityFishHook) {
                EntityFishHook fishHook = (EntityFishHook) packet.getEntity(mc.world);
                if (fishHook.caughtEntity != null && !fishHook.caughtEntity.equals(mc.player)) {
                    packet.processPacket(Objects.requireNonNull(mc.getConnection()));
                }
            }
        }
    }
}