package me.chachoox.lithium.impl.modules.render.killeffect;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

public class ListenerDeath extends ModuleListener<KillEffect, DeathEvent> {
    public ListenerDeath(KillEffect module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.player) {
            EntityLightningBolt bolt = new EntityLightningBolt(mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, false);
            if (module.sound.getValue()) {
                mc.world.playSound(event.getEntity().getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 1.0f, 1.0f, false);
            }
            bolt.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, mc.player.rotationYaw, mc.player.rotationPitch);
            mc.world.spawnEntity(bolt);
        }
    }
}