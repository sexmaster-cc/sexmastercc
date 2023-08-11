package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketVehicleMove;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketVehicleMove.class)
public interface ICPacketVehicleMove {
    @Accessor("y")
    void setY(double y);

    @Accessor("x")
    void setX(double x);

    @Accessor("z")
    void setZ(double z);
}
