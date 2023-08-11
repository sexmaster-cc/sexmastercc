package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayerTryUseItemOnBlock.class)
public interface ICPacketPlayerTryUseItemOnBlock {
    @Accessor("placedBlockDirection")
    void setFacing(EnumFacing facing);
}