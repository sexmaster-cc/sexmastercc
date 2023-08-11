package me.chachoox.lithium.impl.modules.movement.tunnelspeed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

/**
 * @author moneymaker552
 */
public class TunnelSpeed extends Module {

    protected boolean tunnel = false;

    public TunnelSpeed() {
        super("TunnelSpeed", new String[]{"TunnelSpeed", "FastTunnel"}, "Changes x and z motion in tunnels.", Category.MOVEMENT);
        this.offerListeners(new ListenerTick(this));
    }

    public boolean isTunnel() {
        return tunnel;
    }
}
