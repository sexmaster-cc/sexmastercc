package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketEntityHeadLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketEntityHeadLook.class)
public interface ISPacketEntityHeadLook {
    @Accessor("entityId")
    int getEntityId();
}