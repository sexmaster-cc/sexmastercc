package me.chachoox.lithium.api.util.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;
import net.minecraft.util.StringUtils;

public class DiscordPresence implements Minecraftable {

    private final DiscordRPC rpc = DiscordRPC.INSTANCE;
    public final DiscordRichPresence presence = new DiscordRichPresence();

    private String details;
    private String state;

    public boolean isRunning = false;

    public void enable() {
        if (isRunning) {
            return;
        }

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        RichPresence RPC_MODULE = Managers.MODULE.get(RichPresence.class);
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        rpc.Discord_Initialize("1077970844732629043", handlers, true, "");
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.details = Lithium.VERSION;
        presence.state = "Main Menu";
        presence.largeImageKey = "me";
        presence.largeImageText = "SexMaster.CC - " + Lithium.VERSION;

        rpc.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    rpc.Discord_RunCallbacks();
                    details = RPC_MODULE.state.getValue();
                    state = "";
                    if (mc.getCurrentServerData() != null && RPC_MODULE.isServer()) {
                        if (!StringUtils.isNullOrEmpty(mc.getCurrentServerData().serverIP)) {
                            state = "Currently on " + mc.getCurrentServerData().serverIP;
                        }
                    } else {
                        state = "Counting Money";
                    }

                    if (!details.equals(presence.details) || !state.equals(presence.state)) {
                        presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }

                    presence.details = details;
                    presence.state = state;
                    rpc.Discord_UpdatePresence(presence);
                } catch (Exception e2) {
                    e2.printStackTrace();
                } try {
                    //noinspection BusyWait
                    Thread.sleep(5000L);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();

        isRunning = true;
    }

    public void disable() {
        rpc.Discord_Shutdown();
        isRunning = false;
    }
}