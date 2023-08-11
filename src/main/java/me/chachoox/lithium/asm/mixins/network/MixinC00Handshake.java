package me.chachoox.lithium.asm.mixins.network;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.payloadspoof.PayloadSpoof;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @Shadow
    int protocolVersion;

    @Shadow
    String ip;

    @Shadow
    int port;

    @Shadow
    EnumConnectionState requestedState;

    @Inject(method = "writePacketData", at = @At(value = "HEAD"), cancellable = true)
    public void writePacketData(PacketBuffer buf, CallbackInfo info) {
        if (Managers.MODULE.get(PayloadSpoof.class).isEnabled()) {
            info.cancel();
            buf.writeVarInt(protocolVersion);
            buf.writeString(ip);
            buf.writeShort(port);
            buf.writeVarInt(requestedState.getId());
        }
    }

}