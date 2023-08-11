package me.chachoox.lithium.impl.modules.movement.inventorymove;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author moneymaker552
 */
public class InventoryMove extends Module {

    protected final Property<Boolean> crouch =
            new Property<>(
                    false,
                    new String[]{"Sneak", "Crouch"},
                    "Allows sneaking in guis."
            );

    protected final Property<Boolean> jumping =
            new Property<>(
                    false,
                    new String[]{"Jump", "aetra"},
                    "Allows jumping in guis."
            );
    protected final Property<Boolean> sprint =
            new Property<>(
                    false,
                    new String[]{"Sprint", "Run"},
                    "Allows sprinting in guis."
            );

    public InventoryMove() {
        super("InventoryMove", new String[]{"InventoryMove", "InvMove", "Im"}, "Allows you to move in inventories.", Category.MOVEMENT);
        this.offerProperties(sprint, crouch, jumping);
        this.offerListeners(new ListenerClick(this), new ListenerUpdate(this));
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }
}