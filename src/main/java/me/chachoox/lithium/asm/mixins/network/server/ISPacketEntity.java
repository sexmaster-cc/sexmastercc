package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketEntity.class)
public interface ISPacketEntity {
    @Accessor("entityId")
    int getEntityId();
}