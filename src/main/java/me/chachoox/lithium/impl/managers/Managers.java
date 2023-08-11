package me.chachoox.lithium.impl.managers;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.managers.client.*;
import me.chachoox.lithium.impl.managers.config.ConfigManager;
import me.chachoox.lithium.impl.managers.minecraft.CapesManager;
import me.chachoox.lithium.impl.managers.minecraft.DummyManager;
import me.chachoox.lithium.impl.managers.minecraft.RotationManager;
import me.chachoox.lithium.impl.managers.minecraft.chat.ChatManager;
import me.chachoox.lithium.impl.managers.minecraft.combat.PopManager;
import me.chachoox.lithium.impl.managers.minecraft.combat.SwitchManager;
import me.chachoox.lithium.impl.managers.minecraft.exploit.CrashManager;
import me.chachoox.lithium.impl.managers.minecraft.key.KeyManager;
import me.chachoox.lithium.impl.managers.minecraft.movement.ActionManager;
import me.chachoox.lithium.impl.managers.minecraft.movement.KnockbackManager;
import me.chachoox.lithium.impl.managers.minecraft.movement.SpeedManager;
import me.chachoox.lithium.impl.managers.minecraft.movement.StopManager;
import me.chachoox.lithium.impl.managers.minecraft.safe.SafeManager;
import me.chachoox.lithium.impl.managers.minecraft.server.ConnectionManager;
import me.chachoox.lithium.impl.managers.minecraft.server.TPSManager;
import me.chachoox.lithium.impl.managers.minecraft.server.TimerManager;
import org.apache.logging.log4j.Level;

import java.io.File;

public class Managers {

    public static final CommandManager COMMAND = new CommandManager();
    public static final ModuleManager MODULE = new ModuleManager();
    public static final ConfigManager CONFIG = new ConfigManager();
    public static final FriendManager FRIEND = new FriendManager();
    public static final SwitchManager SWITCH = new SwitchManager();
    public static final SafeManager SAFE = new SafeManager();
    public static final PopManager TOTEM = new PopManager();
    public static final RotationManager ROTATION = new RotationManager();
    public static final TPSManager TPS = new TPSManager();
    public static final SpeedManager SPEED = new SpeedManager();
    public static final ThreadManager THREAD = new ThreadManager();
    public static final TimerManager TIMER = new TimerManager();
    public static final DummyManager DUMMY = new DummyManager();
    public static final KnockbackManager KNOCKBACK = new KnockbackManager();
    public static final FontManager FONT = new FontManager();
    public static final ActionManager ACTION = new ActionManager();
    public static final CrashManager CRASH = new CrashManager();
    public static final StopManager STOP = new StopManager();
    public static final CapesManager CAPES = new CapesManager();
    public static final KeyManager KEY = new KeyManager();
    public static final ConnectionManager CONNECTION = new ConnectionManager();
    public static final ChatManager CHAT = new ChatManager();

    public static void load() {
        Logger.getLogger().log(Level.INFO, "Loading managers");
        subscribe(SPEED, COMMAND, TPS, DUMMY, ACTION, ROTATION, TIMER, SAFE, SWITCH, STOP, TOTEM, CRASH, KNOCKBACK, KEY, CONNECTION, CHAT);
        MODULE.init();
        COMMAND.init();
        FRIEND.setDirectory(new File(ConfigManager.PATH, "friends.json"));
        FRIEND.init();
        CONFIG.init();
        Logger.getLogger().log(Level.INFO, "Managers loaded successfully");
    }

    public static void subscribe(Object...subscribers) {
        for (Object subscriber : subscribers) {
            Bus.EVENT_BUS.subscribe(subscriber);
        }
    }
}
