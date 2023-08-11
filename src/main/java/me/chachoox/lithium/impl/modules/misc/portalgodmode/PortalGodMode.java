package me.chachoox.lithium.impl.modules.misc.portalgodmode;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.network.play.client.CPacketConfirmTeleport;

public class PortalGodMode extends Module {

    public PortalGodMode() {
        super("PortalGodMode", new String[]{"PortalGodMode", "portals", "godexploit"}, "Makes you are null and void of attack.", Category.MISC);
        this.listeners.add(new LambdaListener<>(PacketEvent.Send.class, CPacketConfirmTeleport.class, event -> event.setCanceled(true)));
    }
}
