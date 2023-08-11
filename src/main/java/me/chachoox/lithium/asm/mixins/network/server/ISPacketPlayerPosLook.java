package me.chachoox.lithium.asm.mixins.network.server;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketPlayerPosLook.class)
public interface ISPacketPlayerPosLook {
    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    void setPitch(float pitch);
}
