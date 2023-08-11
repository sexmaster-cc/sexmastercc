package me.chachoox.lithium.impl.modules.player.positionspoof;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

/**
 * @author PollosxD
 */
public class PositionSpoof extends Module {

    protected float posX;
    protected float posY;
    protected float posZ;

    public PositionSpoof() {
        super("PositionSpoof", new String[]{"PositionSpoof", "Reposition"}, "Spoofs your current position.", Category.PLAYER);
        this.offerListeners(new ListenerMotion(this), new ListenerCPacket(this));
    }

    @Override
    public void onEnable() {
        this.posX = (float) mc.player.posX;
        this.posY = (float) mc.player.posY;
        this.posZ = (float) mc.player.posZ;
    }
}