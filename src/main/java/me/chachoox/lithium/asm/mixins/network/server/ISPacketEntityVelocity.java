package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketEntityVelocity.class)
public interface ISPacketEntityVelocity {
    @Accessor("entityID")
    int getEntityID();

    @Accessor("motionX")
    int getX();

    @Accessor("motionX")
    void setX(int motionX);

    @Accessor("motionY")
    int getY();

    @Accessor("motionY")
    void setY(int motionY);

    @Accessor("motionZ")
    int getZ();

    @Accessor("motionZ")
    void setZ(int motionZ);
}