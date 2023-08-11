package me.chachoox.lithium.impl.modules.misc.packetcanceller;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;

public class PacketCanceller extends Module {

    protected final Property<Boolean> cTryUseItemOnBlock =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayerTryUseItemOnBlock"}
            );

    protected final Property<Boolean> cDigging =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayerDigging"}
            );

    protected final Property<Boolean> cInput =
            new Property<>(
                    false,
                    new String[]{"CPacketInput"}
            );

    protected final Property<Boolean> cPlayer =
            new Property<>(
                    false,
                    new String[]{"CPacketPlayer"}
            );

    protected final Property<Boolean> cEntityAction =
            new Property<>(
                    false,
                    new String[]{"CPacketEntityAction"}
            );

    protected final Property<Boolean> cUseEntity =
            new Property<>(
                    false,
                    new String[]{"CPacketUseEntity"}
            );

    protected final Property<Boolean> cVehicleMove =
            new Property<>(
                    false,
                    new String[]{"CPacketVehicleMove"}
            );

    protected final Property<Boolean> sCloseWindow =
            new Property<>(
                    false,
                    new String[]{"SPacketCloseWindow"}
            );

    protected int packets = 0;

    public PacketCanceller() {
        super("PacketCanceller", new String[]{"PacketCanceller", "antipackets", "antipacket"}, "Cancels outgoing packets.", Category.MISC);
        this.offerProperties(cTryUseItemOnBlock, cDigging, cInput, cPlayer, cEntityAction, cUseEntity, cVehicleMove, sCloseWindow);
        this.offerListeners(new ListenerSend(this), new ListenerReceive(this));
        for (Property<?> property : this.getProperties()) {
            property.setDescription(String.format("Cancels every %s being sent.", property.getLabel()));
        }
    }

    @Override
    public void onEnable() {
        packets = 0;
    }

    @Override
    public String getSuffix() {
        return "" + packets;
    }
}
