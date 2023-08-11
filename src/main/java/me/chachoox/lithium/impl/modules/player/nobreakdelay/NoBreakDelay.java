package me.chachoox.lithium.impl.modules.player.nobreakdelay;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

public class NoBreakDelay extends Module {

    public NoBreakDelay() {
        super("NoBreakDelay", new String[]{"NoBreakDelay", "AntiBreakDelay", "NoDestroyDelay"}, "Removes break delay interval after you broke a block.", Category.PLAYER);
        this.offerListeners(new ListenerTick(this));
    }
}
