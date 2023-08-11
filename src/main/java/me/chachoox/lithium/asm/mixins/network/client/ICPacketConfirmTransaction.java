package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketConfirmTransaction.class)
public interface ICPacketConfirmTransaction {
    @Accessor("accepted")
    boolean getAccepted();
}
