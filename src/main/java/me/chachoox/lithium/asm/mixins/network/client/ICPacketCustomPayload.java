package me.chachoox.lithium.asm.mixins.network.client;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketCustomPayload.class)
public interface ICPacketCustomPayload {
    @Accessor("data")
    void setData(PacketBuffer data);
}
