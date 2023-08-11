package me.chachoox.lithium.impl.modules.other.rpc;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.discord.DiscordPresence;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.text.TextColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RichPresence extends Module {

    protected List<String> keys = new ArrayList<>();

    public final StringProperty state =
            new StringProperty(
                    "gang shi",
                    new String[]{"State", "stat"}
            );

    public final Property<Boolean> server =
            new Property<>(
                    false,
                    new String[]{"ServerIP", "ip"},
                    "Displays the current server ip."
            );

    private final StopWatch timer = new StopWatch();
    private final StopWatch coolDown = new StopWatch();

    protected final StopWatch imageTimer = new StopWatch();

    public static DiscordPresence RPC;

    public RichPresence() {
        super("RPC", new String[]{"RPC", "discordpresence", "discord", "discordia"}, "Discord presence.", Category.OTHER);
        this.offerProperties(state, server);
        this.offerListeners(new ListenerTick(this));
        this.server.addObserver(event -> {
            if (!timer.passed(10000L) && isEnabled()) {
                Logger.getLogger().log(TextColor.RED + "Wait before you change this again");
                event.setCanceled(true);
                return;
            }
            timer.reset();
        });
        this.initializeKeys();
    }

    @Override
    public void onEnable() {
        if (!coolDown.passed(10000L)) {
            Logger.getLogger().log(TextColor.RED + "Wait before you enable this module again");
            disable();
            return;
        }

        RPC.enable();
        coolDown.reset();
        imageTimer.reset();
    }

    @Override
    public void onDisable() {
        if (RPC == null) {
            return;
        }

        RPC.disable();
    }

    private void initializeKeys() {
        keys.addAll(Arrays.asList("killa", "dead", "jorge", "mateo", "me", "yabujin", "spongenig", "gasnster", "xiuss"));
    }

    public boolean isServer() {
        return isEnabled() && server.getValue();
    }
}
