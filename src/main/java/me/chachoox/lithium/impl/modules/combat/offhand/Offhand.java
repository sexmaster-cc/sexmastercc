package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Offhand extends Module {// IM GOING FUCKING INSANE

    protected final EnumProperty<OffhandMode> mode =
            new EnumProperty<>(
                    OffhandMode.TOTEMS,
                    new String[]{"Offhand", "item", "offhandmode"},
                    "Crystals: - Sets your offhand to crystals / Gapple: - Sets your offhand to golden apples / Totems: - Sets your offhand to totems."
            );

    protected final NumberProperty<Float> health =
            new NumberProperty<>(
                    16f, 1f, 20f, 0.5f,
                    new String[]{"Health", "Hp"},
                    "Threshold that tells us if we should switch to a totem."
            );

    protected final NumberProperty<Float> holeHealth =
            new NumberProperty<>(
                    12f, 1f, 20f, 0.5f,
                    new String[]{"HoleHealth", "HoleHp"},
                    "Threshold that tells us if we should switch to a totem while in holes."
            );

    protected final Property<Boolean> swordGap =
            new Property<>(
                    true,
                    new String[]{"SwordGapple", "Swordgap", "rightclickgap", "rightclickgapple"},
                    "Switches offhand to a gapple if we are holding a sword and pressing right click."
            );

    protected final Property<Boolean> gapOverride =
            new Property<>(
                    true,
                    new String[]{"GappleOverride", "gapoverride"},
                    "Switches offhand to gapple if right click is pressed and overrides the health checker."
            );

    protected final Property<Boolean> lethal =
            new Property<>(
                    false,
                    new String[]{"Lethal", "ExtraCalc", "Safety"},
                    "Switches to a totem if we are going to pop from falling."
            );

    protected final Property<Boolean> mainhand =
            new Property<>(
                    false,
                    new String[]{"Mainhand", "why is this here"},
                    "Switches mainhand to a totem."
            );

    protected final StopWatch timer = new StopWatch();
    protected boolean gap;

    public Offhand() {
        super("Offhand", new String[]{"offhand", "autototem", "offhandcrystal", "offhandgap"}, "Cycles offhand items.", Category.COMBAT);
        this.offerProperties(mode, health, holeHealth, swordGap, gapOverride, lethal, mainhand);
        this.offerListeners(new ListenerInteract(this), new ListenerGameLoop(this));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    protected void windowClick(int slot) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
    }

    protected void mainhandTotem(int slot) {
        ItemStack stack = mc.player.inventory.getStackInSlot(slot);

        if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }

        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, slot, ClickType.SWAP, mc.player);
            }
        }
    }

    protected int getItemSlot(Item itemIn) {
        for (int i = 45; i > 0; i--) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemIn) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    protected Item getItem(boolean safe, boolean gapple) {
        Item item = Items.TOTEM_OF_UNDYING;

        if (lethal.getValue()) {
            if (!safe) {
                return item;
            }
        }

        if (lethal.getValue() && mc.player.fallDistance > 10) {
            return item;
        }

        boolean inHole = HoleUtil.isHole(mc.player.getPosition());
        if (EntityUtil.getHealth(mc.player) >= getHealth(inHole, gapple, gapOverride.getValue())) {
            item = gapple ? Items.GOLDEN_APPLE : mode.getValue().item;
        }

        return item;
    }

    protected float getHealth(boolean safe, boolean gapple, boolean antigap) {
        return gapple ? (antigap ? 0.0F : (safe ? holeHealth.getValue() : health.getValue())) : (safe ? holeHealth.getValue() : health.getValue());
    }
}

