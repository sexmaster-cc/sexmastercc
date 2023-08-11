package me.chachoox.lithium.impl.modules.other.blocks;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.module.PersistentModule;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.modules.player.autofeetplace.AutoFeetPlace;
import me.chachoox.lithium.impl.modules.other.blocks.util.Debugger;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.Level;

//TODO: rewrite this module and every block place module to implement this module in a better way/\\/\/\/\\/ jsut make it extend from the util or something
public class BlocksManager extends PersistentModule {

    private final Property<Boolean> enderChests =
            new Property<>(
                    false,
                    new String[]{"EnderChests", "chests", "echests"},
                    "Places enderchests with other modules that arent surround."
            );

    private final Property<Boolean> disableOnDeath =
            new Property<>(
                    false,
                    new String[]{"DisableOnDeath", "DeathDisable", "AutoDeathDisable"},
                    "Disables block place modules when you die."
            );

    private final Property<Boolean> disableOnDisconnect =
            new Property<>(
                    false,
                    new String[]{"DisableOnDisconnect", "DisconnectDisable", "LeaveDisable", "DisableOnLeave"},
                    "Disables block place modules when you disconnect."
            );

    private final Property<Boolean> debugSurround =
            new Property<>(
                    false,
                    new String[]{"DebugSurround"}
            );

    private final Property<Boolean> debugSelfFill =
            new Property<>(
                    false,
                    new String[]{"DebugSelfFill"}
            );

    private final Property<Boolean> debugSelfBlocker =
            new Property<>(
                    false,
                    new String[]{"DebugSelfBlocker"}
            );

    private final Property<Boolean> debugScaffold =
            new Property<>(
                    false,
                    new String[]{"DebugScaffold"}
            );

    private final Property<Boolean> debugTrap =
            new Property<>(
                    false,
                    new String[]{"DebugTrap"}
            );

    private final Property<Boolean> debugHoleFill =
            new Property<>(
                    false,
                    new String[]{"DebugHoleFill"}
            );

    private final Property<Boolean> debugWeb =
            new Property<>(
                    false,
                    new String[]{"DebugWeb"}
            );


    private final EnumProperty<Debugger> debugger =
            new EnumProperty<>(
                    Debugger.NONE,
                    new String[]{"Debug"},
                    "Debugs block placements in chat or logs."
            );

    private static BlocksManager BLOCK_MANAGER;

    public BlocksManager() {
        super("Blocks", new String[]{"Blocks", "block", "blockmanage", "blocksmanager"}, "Manages obsidian & ender chest placing modules.", Category.OTHER);
        this.offerProperties(enderChests, disableOnDeath, disableOnDisconnect, debugSurround, debugSelfFill, debugSelfBlocker, debugScaffold, debugTrap, debugHoleFill, debugWeb, debugger);
        for (Property<?> property : this.getProperties()) {
            if (StringUtils.isNullOrEmpty(property.getLabel())) {
                property.setDescription("Renders " + property.getLabel().replace("Debug", "") + ".");
            }
        }
        BLOCK_MANAGER = this;
    }

    public static BlocksManager get() {
        return (BLOCK_MANAGER == null) ? (BLOCK_MANAGER = new BlocksManager()) : BLOCK_MANAGER;
    }

    public void log(String message) {
        switch (debugger.getValue()) {
            case LOG: {
                Logger.getLogger().log(Level.INFO, "<Blocks> " + message);
                break;
            }
            case CHAT: {
                Logger.getLogger().log("<Blocks> " + message, false);
                break;
            }
        }
    }

    public Boolean placeEnderChests(Module module) {
        return enderChests.getValue() || module instanceof AutoFeetPlace;
    }

    public Boolean disableOnDeath() {
        return disableOnDeath.getValue();
    }

    public Boolean disableOnDisconnect() {
        return disableOnDisconnect.getValue();
    }

    public Boolean debugTrap() {
      return debugTrap.getValue();
    }

    public Boolean debugHoleFill() {
        return debugHoleFill.getValue();
    }

    public Boolean debugScaffold() {
        return debugScaffold.getValue();
    }

    public Boolean debugSurround() {
        return debugSurround.getValue();
    }

    public Boolean debugSelfFill() {
        return debugSelfFill.getValue();
    }

    public Boolean debugSelfBlocker() {
        return debugSelfBlocker.getValue();
    }

    public Boolean debugWeb() {
        return debugWeb.getValue();
    }
}
