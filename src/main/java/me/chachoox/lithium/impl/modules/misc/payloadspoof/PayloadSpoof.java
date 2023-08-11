package me.chachoox.lithium.impl.modules.misc.payloadspoof;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

public class PayloadSpoof extends Module {

    public PayloadSpoof() {
        super("PayloadSpoof", new String[]{"PayloadSpoof", "nohandshake", "thugshaker"}, "Sends a fake payload when joining a server.", Category.MISC);
        this.offerListeners(new ListenerForge(this), new ListenerCustomPayload(this));
    }

}
