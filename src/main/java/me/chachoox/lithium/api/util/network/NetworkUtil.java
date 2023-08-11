package me.chachoox.lithium.api.util.network;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.pingspoof.PingSpoof;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Level;

public class NetworkUtil implements Minecraftable {

    public static int getLatency() {
        try {
            NetHandlerPlayClient connection = mc.getConnection();
            if (connection != null) {
                NetworkPlayerInfo info = connection
                        .getPlayerInfo(mc.getConnection()
                                .getGameProfile()
                                .getId());
                //noinspection ConstantConditions
                if (info != null) {
                    return info.getResponseTime();
                }
            }
        } catch (Throwable t) {
            Logger.getLogger().log(Level.ERROR, "Failed to get latency");
        }
        return 0;
    }

    public static int getLatencyNoSpoof() {
        final PingSpoof PING_SPOOF = Managers.MODULE.get(PingSpoof.class);
        int ping = getLatency();
        ping -= PING_SPOOF.getLatency();
        if (PING_SPOOF.isEnabled()) {
            return ping;
        } else {
            return getLatency();
        }
    }

    public static void disconnect(String message) {
        if (mc.getConnection() != null) {
            mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString(message)));
        }
    }
}
