package me.chachoox.lithium.impl.managers.minecraft.server;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.apache.logging.log4j.Level;

import java.util.UUID;

public class ConnectionManager extends SubscriberImpl implements Minecraftable {
    public ConnectionManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketPlayerListItem>>(PacketEvent.Receive.class, Integer.MAX_VALUE, SPacketPlayerListItem.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketPlayerListItem> event) {
                SPacketPlayerListItem packet = event.getPacket();
                if (mc.world == null
                        || SPacketPlayerListItem.Action.ADD_PLAYER != packet.getAction()
                        && SPacketPlayerListItem.Action.REMOVE_PLAYER != packet.getAction()) {
                    return;
                }

                try {
                    for (SPacketPlayerListItem.AddPlayerData data : packet.getEntries()) {
                        switch (packet.getAction()) {
                            case ADD_PLAYER:
                                final UUID joinUUID = data.getProfile().getId();
                                final String joinName = data.getProfile().getName();
                                final EntityPlayer joinPlayer = mc.world.getPlayerEntityByUUID(joinUUID);
                                scheduleEvent(new ConnectionEvent.Join(joinName, joinUUID, joinPlayer));
                                break;
                            case REMOVE_PLAYER:
                                NetworkPlayerInfo info = null;
                                for (NetworkPlayerInfo playerInfo : mc.player.connection.getPlayerInfoMap()) {
                                    if (!playerInfo.getGameProfile().getId().equals(data.getProfile().getId()))
                                        continue;
                                    info = playerInfo;
                                    break;
                                }

                                if (info == null) continue;

                                final UUID leaveUUID = info.getGameProfile().getId();
                                final String leaveName = info.getGameProfile().getName();
                                final EntityPlayer leavePlayer = mc.world.getPlayerEntityByUUID(leaveUUID);
                                scheduleEvent(new ConnectionEvent.Leave(leaveName, leaveUUID, leavePlayer));
                                break;
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger().log(Level.ERROR, "Error while handling connection event");
                }
            }
        });
    }

    private void scheduleEvent(ConnectionEvent event) {
        mc.addScheduledTask(() -> Bus.EVENT_BUS.dispatch(event));
    }
}

