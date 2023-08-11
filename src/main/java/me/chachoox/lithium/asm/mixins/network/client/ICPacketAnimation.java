package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketAnimation.class)
public interface ICPacketAnimation {
    @Accessor("hand")
    void setHand(EnumHand hand);
}
