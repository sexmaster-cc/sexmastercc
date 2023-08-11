package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface ICPacketPlayer {
    @Accessor("yaw")
    float getYaw();

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    float getPitch();

    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("x")
    void setX(double x);

    @Accessor("x")
    double getX();

    @Accessor("y")
    void setY(double y);

    @Accessor("y")
    double getY();

    @Accessor("z")
    void setZ(double z);

    @Accessor("z")
    double getZ();

    @Accessor("onGround")
    void setOnGround(boolean onGround);

    @Accessor("rotating")
    boolean isRotating();

    @Accessor("moving")
    boolean isMoving();
}
