package me.chachoox.lithium.asm.ducks;

import net.minecraft.network.Packet;

public interface INetworkManager {
    Packet<?> sendPacketNoEvent(Packet<?> packetIn);
}