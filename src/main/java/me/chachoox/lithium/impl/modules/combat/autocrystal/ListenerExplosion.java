package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketExplosion;

import java.util.ArrayList;

public class ListenerExplosion extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketExplosion>> {
    public ListenerExplosion(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketExplosion.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketExplosion> event) {
        SPacketExplosion packet = event.getPacket();
        if (packet.getStrength() == 6.0f) {
            for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                if (!module.attackMap.containsKey(entity.getEntityId())) {
                    return;
                }
                if (entity instanceof EntityEnderCrystal && entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0d) {
                    mc.addScheduledTask(entity::setDead);
                    mc.addScheduledTask(() -> mc.world.removeEntity(entity));
                }
            }
        }
    }
}
