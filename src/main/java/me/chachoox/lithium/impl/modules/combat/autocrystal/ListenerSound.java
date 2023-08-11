package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;

public class ListenerSound extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        final SPacketSoundEffect packet = event.getPacket();
        if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityEnderCrystal) {
                    if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) < 36) {
                        entity.setDead();
                        mc.world.removeEntity(entity);
                    }
                }
            }
        }
    }
}
