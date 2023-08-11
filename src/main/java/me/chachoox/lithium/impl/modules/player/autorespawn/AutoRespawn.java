package me.chachoox.lithium.impl.modules.player.autorespawn;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.client.gui.GuiGameOver;

/**
 * @author moneymaker552
 */
public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", new String[]{"AutoRespawn", "AntiDeathScreen", "NoDeathScreen", "FastRespawn"}, "Automatically respawns when you are dead.", Category.PLAYER);
        this.offerListeners(new LambdaListener<>(DeathEvent.class, event -> {
            if (event.getEntity() != mc.player && mc.player == null) {
                return;
            }
            if (event.getEntity() == mc.player) {
                mc.player.respawnPlayer();
            }
        }));
        this.listeners.add(new LambdaListener<>(GuiScreenEvent.class, GuiGameOver.class, event -> event.setCanceled(true)));
    }
}
