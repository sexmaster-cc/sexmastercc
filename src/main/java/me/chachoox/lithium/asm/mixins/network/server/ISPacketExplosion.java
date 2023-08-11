package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketExplosion.class)
public interface ISPacketExplosion {
    @Accessor("motionX")
    float getMotionX();

    @Accessor("motionX")
    void setMotionX(float motionX);

    @Accessor("motionY")
    float getMotionY();

    @Accessor("motionY")
    void setMotionY(float motionY);

    @Accessor("motionZ")
    float getMotionZ();

    @Accessor("motionZ")
    void setMotionZ(float motionZ);
}