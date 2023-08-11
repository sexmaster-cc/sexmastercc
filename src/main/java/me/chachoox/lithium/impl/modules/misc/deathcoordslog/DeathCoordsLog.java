package me.chachoox.lithium.impl.modules.misc.deathcoordslog;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.client.gui.GuiGameOver;

/**
 * @author moneymaker552
 */
public class DeathCoordsLog extends Module {

    private String deathPosition;

    public DeathCoordsLog() {
        super("DeathCoordsLog", new String[]{"DeathCoordsLog", "deathlog", "dcl"}, "Logs your coordinates of where you died.", Category.MISC);
        this.offerListeners(new LambdaListener<>(GuiScreenEvent.class, GuiGameOver.class, event -> {
            deathPosition = String.format("X: %s, Y: %s, Z: %s", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ);
            Logger.getLogger().logNoMark(String.format("%s<%s> You died at -> (%s)", TextColor.RED, getLabel(), deathPosition), false);
        }));
    }

    public String getDeathPosition() {
        return deathPosition;
    }

    public Boolean hasDied() {
        return deathPosition != null;
    }
}
