package me.chachoox.lithium.impl.modules.misc.packetlogger;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Logging;
import me.chachoox.lithium.impl.modules.misc.packetlogger.mode.Packets;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class PacketLogger extends Module {

    protected final EnumProperty<Packets> packets =
            new EnumProperty<>(
                    Packets.OUTGOING,
                    new String[]{"Packets", "packet"},
                    "What type of packets log. (Incoming: Server side packets / Outgoing: Client side packets)"
            );

    protected final EnumProperty<Logging> logging =
            new EnumProperty<>(
                    Logging.CHAT,
                    new String[]{"Logging", "log"},
                    "Chat: packets will be print in chat / File: packets are stored in a file at (Lithium/Packets/packet_log.txt)"
            );

    protected final Property<Boolean> showCanceled =
            new Property<>(false,
                    new String[]{"Canceled", "cancel"},
                    "Writes if the packet was cancelled or not"
            );

    /**
     * CLIENT
     */

    protected final Property<Boolean> cPlayer =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayer", "cplayer"}
            );

    protected final Property<Boolean> cUseEntity =
            new Property<>(
                    false,
                    new String[]{"CPacketUseEntity", "cuseentity", "cattack"}
            );

    protected final Property<Boolean> cTryUseItemOnBlock =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayerTryUseItemOnBlock", "cplayertryuseitemonblock", "cplace"}
            );

    protected final Property<Boolean> cDigging =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayerDigging", "cplayerdig", "cdig"}
            );

    protected final Property<Boolean> cHeldItem =
            new Property<>(
                    false,
                    new String[]{"CPacketHeldItemChange", "citemchamge"}
            );

    protected final Property<Boolean> cCloseWindow =
            new Property<>(
                    false,
                    new String[]{"CPacketCloseWindow", "cclosewindow"}
            );

    protected final Property<Boolean> cClickWindow =
            new Property<>(
                    false,
                    new String[]{"CPacketClickWindow", "cclickwindow"}
            );

    protected final Property<Boolean> cEntityAction =
            new Property<>(
                    false,
                    new String[]{"CPacketEntityAction", "centityaction"}
            );

    protected final Property<Boolean> cUseItem =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayerTryUseItem", "cuseitem"}
            );

    protected final Property<Boolean> cAnimation =
            new Property<>(
                    false,
                    new String[]{"CPacketAnimation", "canimation"}
            );

    protected final Property<Boolean> cConfirmTransaction =
            new Property<>(
                    false,
                    new String[]{"CPacketConfirmTransaction", "cconfirmtransaction"}
            );

    protected final Property<Boolean> cConfirmTeleport =
            new Property<>(
                    false,
                    new String[]{"CPacketConfirmTeleport", "cconfirmteleport"}
            );

    /**
     * SERVER
     */

    protected final Property<Boolean> sPlayerPosLook =
            new Property<>(
                    false,
                    new String[]{"SPacketPlayerPosLook", "spacketposlook"}
            );

    protected final Property<Boolean> sPlayerListItem =
            new Property<>(
                    false,
                    new String[]{"SPacketPlayerListItem", "spacketplayerlist"}
            );

    protected final Property<Boolean> sOpenWindow =
            new Property<>(
                    false,
                    new String[]{"SPacketOpenWindow", "sopenwindow"}
            );

    protected final Property<Boolean> sCloseWindow =
            new Property<>(
                    false,
                    new String[]{"SPacketCloseWindow", "sclosewindow"}
            );

    protected final Property<Boolean> sSetSlot =
            new Property<>(
                    false,
                    new String[]{"SPacketSetSlot", "ssetslot"}
            );

    protected final Property<Boolean> sEntityStatus =
            new Property<>(
                    false,
                    new String[]{"SPacketEntityStatus", "sentitystatus"}
            );

    protected final Property<Boolean> sPacketResource =
            new Property<>(
                    false,
                    new String[]{"SPacketResourcePackSend", "spacketresource"}
            );

    protected final Property<Boolean> sConfirmTransaction =
            new Property<>(
                    false,
                    new String[]{"SPacketConfirmTransaction", "sconfirmtransaction"}
            );

    private final File PACKET_LOG = new File(Lithium.PACKETS, "packet_log.txt");
    private BufferedWriter writer;

    public PacketLogger() {
        super("PacketLogger", new String[]{"PacketLogger", "packetprinter", "logger"}, "Prints packets in chat.", Category.MISC);
        this.offerProperties(
                packets, logging, showCanceled,
                cPlayer, cUseEntity, cTryUseItemOnBlock, cDigging, cHeldItem, cCloseWindow, cClickWindow, cEntityAction, cUseItem, cAnimation, cConfirmTransaction, cConfirmTeleport,
                sPlayerPosLook, sPlayerListItem, sOpenWindow, sCloseWindow, sSetSlot, sEntityStatus, sPacketResource, sConfirmTransaction
        );
        this.offerListeners(new ListenerSend(this), new ListenerReceive(this));
        for (Property<?> property : this.getProperties()) {
            if (StringUtils.isNullOrEmpty(property.getDescription())) {
                property.setDescription(String.format("Prints every %s in chat.", property.getLabel()));
            }
        }
        initializeFile();
        this.logging.addObserver(event-> {
            if (!event.isCanceled()) {
                clear();
            }
        });
    }

    @Override
    public void onEnable() {
        initializeWriter();
    }

    @Override
    public void onDisable() {
        clear();
    }

    protected void log(String message) {
        if (logging.getValue() == Logging.FILE) {
            return;
        }

        Logger.getLogger().logNoMark(message, false);
    }

    private void clear() {
        try {
            PACKET_LOG.delete();
            PACKET_LOG.createNewFile();
            writer.flush();
            writer.close();
            writer = null;
        } catch (Exception ex) {
            Logger.getLogger().log(Level.ERROR, "Failed to clear packet log files");
        }
    }

    protected void initializeWriter() {
        if (writer == null) {
            try {
                writer = new BufferedWriter(new FileWriter(PACKET_LOG));
            } catch (Exception e) {
                Logger.getLogger().log(Level.ERROR, "Failed to create writer");
            }
        }
    }

    private void initializeFile() {
        try {
            if (!PACKET_LOG.exists()) {
                PACKET_LOG.createNewFile();
            } else {
                PACKET_LOG.delete();
            }
        } catch (IOException e) {
            Logger.getLogger().log(Level.ERROR, "Couldn't make packet log file");
        }
    }

    protected void write(String message) {
        if (logging.getValue() == Logging.CHAT) {
            return;
        }

        try {
            String s = message + "\n";
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            Logger.getLogger().log(Level.ERROR, "Error while writing packet log text");
        }
    }
}
