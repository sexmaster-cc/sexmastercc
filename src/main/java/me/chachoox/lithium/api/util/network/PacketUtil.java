package me.chachoox.lithium.api.util.network;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.INetworkManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;

public class PacketUtil implements Minecraftable {

    public static void send(Packet<?> packet) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null) {
            connection.sendPacket(packet);
        }
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static Packet<?> sendPacketNoEvent(Packet<?> packet) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null) {
            INetworkManager manager = (INetworkManager) connection.getNetworkManager();
            return manager.sendPacketNoEvent(packet);
        }

        return null;
    }

    public static void sneak(boolean sneak) {
        send(new CPacketEntityAction(mc.player, sneak ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
    }

    public static void swing() {
        send(new CPacketAnimation(EnumHand.MAIN_HAND));
    }
}
