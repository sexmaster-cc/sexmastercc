package me.chachoox.lithium.impl.modules.combat.autoarmour;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.MendingStage;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.SingleMendingSlot;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.WindowClick;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

public class AutoArmour extends Module {

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(2, 1, 10,
                    new String[]{"Delay", "delayjamin"},
                    "Delay between selecting armor pieces."
            );

    protected final Property<Boolean> autoMend =
            new Property<>(
                    false,
                    new String[]{"AutoMend", "crystalpvpnnmode"},
                    "Automatically takes off armor pieces when mending to not waste exp."
            );

    protected final Property<Boolean> singleMend =
            new Property<>(
                    false,
                    new String[]{"SingleMend", "crystalpvpnnmodepartTWO"},
                    "Only mends one armor piece at a time."
            );

    protected final NumberProperty<Integer> helmet =
            new NumberProperty<>(
                    99, 1, 100,
                    new String[]{"Helmet%", "Hat%", "HelmetPercent", "HatPercent", "HatPercentage", "HelmetPercentage"},
                    "Threshold percentage for helmets."
            );

    protected final NumberProperty<Integer> chest =
            new NumberProperty<>(
                    95, 1, 100,
                    new String[]{"Chest%", "Chestplate%", "ChestplatePercent", "ChestPercent", "ChestPercentage", "ChestplatePercentage"},
                    "Threshold percentage for chestplates."
            );

    protected final NumberProperty<Integer> pants =
            new NumberProperty<>(
                    97, 1, 100,
                    new String[]{"Pants%", "Leggings%", "LeggingsPercent", "PantsPercent", "PantsPercentage", "LeggingsPercentage"},
                    "Threshold percentage for leggings."
            );

    protected final NumberProperty<Integer> boots =
            new NumberProperty<>(
                    97, 1, 100,
                    new String[]{"Boots%", "Shoes%", "ShoesPercent", "BootsPercent", "BootsPercentage", "ShoePercentage"},
                    "Threshold percentage for boots."
            );

    protected final Property<Boolean> safe =
            new Property<>(
                    true,
                    new String[]{"Safe", "NoCrystals", "PlayerCheck", "CrystalCheck", "NoPlayers", "crystalppvnnbiggestenemy"},
                    "Wont automend if all safety checks havent passed."
            );

    protected final Property<Boolean> drag =
            new Property<>(
                    false,
                    new String[]{"Drag", "Pull", "XCarry"},
                    "Uses the drag slot more plus xcarry slots to store stuff if we can."
            );

    protected final Property<Boolean> putBack =
            new Property<>(
                    true,
                    new String[]{"Putback", "NoDrag", "AntiStuck"},
                    "Prevents items from getting stuck in the drag slot."
            );

    protected final Property<Boolean> doubleClick =
            new Property<>(
                    false,
                    new String[]{"DoubleClick", "Click", "ExtraClick"},
                    "Queues the polled windowclicks after the first click."
            );

    protected final Property<Boolean> strict =
            new Property<>(
                    false,
                    new String[]{"Strict", "strictjamin", "NoXCarry"},
                    "Avoids the xcarry slot when looking and iterating items."
            );

    protected final Property<Boolean> pauseInInv =
            new Property<>(
                    false,
                    new String[]{"PauseInInv", "PauseInInventory", "InvPause"},
                    "Stops putting on armor and automending if our inventory is open."
            );

    protected final StopWatch timer = new StopWatch();

    protected final Queue<WindowClick> windowClicks = new LinkedList<>();
    protected Set<Integer> queuedSlots = new HashSet<>();

    protected Property<?>[] damages;
    protected WindowClick putBackClick;

    protected boolean stackSet;

    protected MendingStage stage = MendingStage.MENDING;
    protected final StopWatch mendingTimer = new StopWatch();

    protected final SingleMendingSlot[] singleMendingSlots = {
            new SingleMendingSlot(EntityEquipmentSlot.HEAD),
            new SingleMendingSlot(EntityEquipmentSlot.CHEST),
            new SingleMendingSlot(EntityEquipmentSlot.LEGS),
            new SingleMendingSlot(EntityEquipmentSlot.FEET)
    };

    public AutoArmour() {
        super("AutoArmour", new String[]{"AutoArmour", "aa", "armor"}, "Puts armour on automatically.", Category.COMBAT);
        this.damages = new Property[]{helmet, chest, pants, boots};
        this.offerProperties(delay, autoMend, singleMend, helmet, chest, pants, boots, safe, drag, putBack, doubleClick, strict, pauseInInv);
        this.offerListeners(new ListenerTick(this), new ListenerGameLoop(this));
    }

    @Override
    public void onEnable() {
        stage = MendingStage.MENDING;
        windowClicks.clear();
        queuedSlots.clear();
        putBackClick = null;
        unblockMendingSlots();
    }

    @Override
    public void onDisable() {
        stage = MendingStage.MENDING;
        windowClicks.clear();
        queuedSlots.clear();
        putBackClick = null;
        unblockMendingSlots();
    }

    protected void unblockMendingSlots() {
        for (SingleMendingSlot mendingSlot : singleMendingSlots) {
            mendingSlot.setBlocked(false);
        }
    }

    protected WindowClick queueClick(int slot, ItemStack inSlot, ItemStack inDrag) {
        return queueClick(slot, inSlot, inDrag, slot);
    }

    protected WindowClick queueClick(int slot, ItemStack inSlot, ItemStack inDrag, int target) {
        WindowClick click = new WindowClick(slot, inSlot, inDrag, target);
        queueClick(click);
        click.setFast(strict.getValue());
        return click;
    }

    protected void queueClick(WindowClick click) {
        windowClicks.add(click);
    }

    protected void runClick() {
        if (InventoryUtil.validScreen() && mc.playerController != null) {
            if (timer.passed(delay.getValue() * 10L)) {
                WindowClick windowClick = windowClicks.poll();
                while (windowClick != null) {
                    if (safe.getValue() && !windowClick.isValid()) {
                        windowClicks.clear();
                        queuedSlots.clear();
                        return;
                    }

                    windowClick.runClick(mc.playerController);
                    timer.reset();

                    if (!windowClick.isDoubleClick()) {
                        return;
                    }

                    windowClick = windowClicks.poll();
                }
            }
        } else {
            windowClicks.clear();
            queuedSlots.clear();
        }
    }

    protected ItemStack setStack() {
        if (!stackSet) {
            ItemStack drag = mc.player.inventory.getItemStack();
            if (!drag.isEmpty()) {
                int slot = findItem(Items.AIR, !strict.getValue(), queuedSlots);
                if (slot != -1) {
                    ItemStack inSlot = InventoryUtil.get(slot);
                    queueClick(slot, drag, inSlot);
                    queuedSlots.add(slot);
                    stackSet = true;
                    return inSlot;
                }

                return null;
            }

            stackSet = true;
            return drag;
        }

        return null;
    }

    protected boolean canAutoMend() { // make this work with InstantEXP ?
        if (!autoMend.getValue() || (!Mouse.isButtonDown(1) || !InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE))) {
            return false;
        }

        EntityPlayer closestPlayer = EntityUtil.getClosestEnemy();
        if (closestPlayer != null && closestPlayer.getDistanceSq(mc.player) < MathUtil.square(4.5F * 2)) {
            return false;
        }

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(mc.player) < MathUtil.square(6.5F * 2) && !entity.isDead && mc.player.getDistanceSq(entity) <= 144) {
                return false;
            } else if (entity instanceof EntityEnderPearl && entity.getDistanceSq(mc.player) < MathUtil.square(32.5F * 2) && !entity.isDead && mc.player.getDistanceSq(entity) <= 144) {
                return false;
            }
        }

        return true;
    }

    protected static EntityEquipmentSlot fromSlot(int slot) {
        switch (slot) {
            case 5:
                return EntityEquipmentSlot.HEAD;
            case 6:
                return EntityEquipmentSlot.CHEST;
            case 7:
                return EntityEquipmentSlot.LEGS;
            case 8:
                return EntityEquipmentSlot.FEET;
            default:
                ItemStack stack = InventoryUtil.get(slot);
                return getSlot(stack);
        }
    }

    protected static int fromEquipment(EntityEquipmentSlot equipmentSlot) {
        switch (equipmentSlot) {
            case OFFHAND:
                return 45;
            case FEET:
                return 8;
            case LEGS:
                return 7;
            case CHEST:
                return 6;
            case HEAD:
                return 5;
            default:
        }

        return -1;
    }

    protected static EntityEquipmentSlot getSlot(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                return armor.getEquipmentSlot();
            } else if (stack.getItem() instanceof ItemElytra) {
                return EntityEquipmentSlot.CHEST;
            }
        }

        return null;
    }

    public static int findItem(Item item, boolean xCarry, Set<Integer> blackList) {
        ItemStack drag = mc.player.inventory.getItemStack();
        if (!drag.isEmpty() && drag.getItem() == item && !blackList.contains(-2)) {
            return -2;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = InventoryUtil.get(i);
            if (stack.getItem() == item && !blackList.contains(i)) {
                return i;
            }
        }

        if (xCarry) {
            for (int i = 1; i < 5; i++) {
                ItemStack stack = InventoryUtil.get(i);
                if (stack.getItem() == item && !blackList.contains(i)) {
                    return i;
                }
            }
        }

        return -1;
    }

    protected static int iterateItems(boolean xCarry, Set<Integer> blackList, Function<ItemStack, Boolean> accept) {
        ItemStack drag = mc.player.inventory.getItemStack();
        if (!drag.isEmpty() && !blackList.contains(-2) && accept.apply(drag)) {
            return -2;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = InventoryUtil.get(i);
            if (!blackList.contains(i) && accept.apply(stack)) {
                return i;
            }
        }

        if (xCarry) {
            for (int i = 1; i < 5; i++) {
                ItemStack stack = InventoryUtil.get(i);
                if (!blackList.contains(i) && accept.apply(stack)) {
                    return i;
                }
            }
        }

        return -1;
    }

}
