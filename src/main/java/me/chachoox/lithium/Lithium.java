package me.chachoox.lithium;

import me.chachoox.lithium.api.util.discord.DiscordPresence;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.key.KeyManager;
import me.chachoox.lithium.impl.modules.other.rpc.RichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import java.io.File;

@Mod(name = "Lithium", modid = "lithium", version = Lithium.VERSION)
public class Lithium {
    public static final String NAME = "Lithium";
    public static final String VERSION = "v0.8.0";
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().gameDir.getAbsolutePath(), "Lithium");
    public static final File SPAMMER = new File(DIRECTORY, "spammers");
    public static final File PACKETS = new File(DIRECTORY, "packets");

    public static boolean firstTimeLoaded = false;

    public static void load() {
        long startTime = System.nanoTime() / 1000000L;
        if (!DIRECTORY.exists()) {
            firstTimeLoaded = true;
            Logger.getLogger().log(Level.INFO, String.format("%s client directory.", DIRECTORY.mkdir() ? "Created" : "Failed to create"));
        }

        if (!SPAMMER.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s spammer directory.", SPAMMER.mkdir() ? "Created" : "Failed to create"));
        }

        if (!PACKETS.exists()) {
            Logger.getLogger().log(Level.INFO, String.format("%s packets directory.", PACKETS.mkdir() ? "Created" : "Failed to create"));
        }

        if (!System.getProperty("os.arch").equals("aarch64")) {
            RichPresence.RPC = new DiscordPresence();
        } else {
            RichPresence.RPC = null;
        }

        KeyManager.KIT_DELETE_BIND = new KeyBinding("DeleteKit", Keyboard.KEY_BACK, "KitDelete");
        ClientRegistry.registerKeyBinding(KeyManager.KIT_DELETE_BIND);
        Managers.load();
        Logger.getLogger().log(Level.INFO, String.format("Initialized in %s milliseconds.", System.nanoTime() / 1000000L - startTime));
        Logger.getLogger().log(Level.INFO, "\"yogurt\"");
        Logger.getLogger().log(Level.INFO, "gurt: yo wassup");
    }
}
