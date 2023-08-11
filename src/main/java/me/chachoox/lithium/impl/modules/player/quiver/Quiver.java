package me.chachoox.lithium.impl.modules.player.quiver;

import com.google.common.collect.Sets;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.text.TextUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Quiver extends Module {

    protected static final PotionType SPECTRAL = new PotionType();
    protected static final Set<PotionType> BAD_TYPES = Sets.newHashSet(
            PotionTypes.EMPTY,
            PotionTypes.WATER,
            PotionTypes.MUNDANE,
            PotionTypes.THICK,
            PotionTypes.AWKWARD,
            PotionTypes.HEALING,
            PotionTypes.STRONG_HEALING,
            PotionTypes.STRONG_HARMING,
            PotionTypes.HARMING
    );

    private final Set<String> arStrings = new HashSet<>();
//Godd I hate my fucking life
    protected final NumberProperty<Integer> releaseTicks =
        new NumberProperty<>(
                3, 0, 20,
                new String[]{"ReleaseTicks", "releasetick", "rt"},
                "How long it takes to release the bow."
        );

    protected final NumberProperty<Integer> maxTicks =
            new NumberProperty<>(
                    10, 0, 20,
                    new String[]{"MaxTicks", "maxtick", "mt"},
                    "If shooting the bow fails and this many ticks passed we cancel shooting the bow."
            );

    protected final NumberProperty<Integer> cancelTime =
            new NumberProperty<>(
                    0, 0, 500,
                    new String[]{"CancelTime", "waitTime", "pausetime", "pt", "wt"},
                    "How long we will cancel shooting the bow for if we are cycling with 2 arrows of different types."
            );

    protected final NumberProperty<Integer> cycleDelay =
            new NumberProperty<>(
                    250, 0, 500,
                    new String[]{"CycleDelay", "Cd", "CycleDel"},
                    "The delay between cycling 2 arrows."
            );

    protected final NumberProperty<Integer> shootDelay =
            new NumberProperty<>(
                    500, 0, 500,
                    new String[]{"ShootDelay", "shotdelay", "imgoingfuckinginsanae"},
                    "The delay after shooting an arrow."
            );

    protected final BindProperty cycleButton =
            new BindProperty(
                    new Bind(Keyboard.KEY_NONE),
                    new String[]{"CycleKey", "Cyclekeybind", "cycle"},
                    "Cycles arrows whenever we press this key instantly."
            );

    protected final Set<PotionType> cycled = new HashSet<>();
    protected final StopWatch cycleTimer = new StopWatch();
    protected final StopWatch timer = new StopWatch();

    protected boolean fast;

    public Quiver() {
        super("Quiver", new String[]{"Quiver", "SelfBow", "Arrows", "InstantSelfBow"}, "Automatically shoots valid arrows at yourself.", Category.PLAYER);
        this.offerProperties(releaseTicks, maxTicks, cancelTime, cycleDelay, shootDelay, cycleButton);
        this.offerListeners(new ListenerMotion(this), new ListenerKeyboard(this), new ListenerUseItem(this));
    }

    @Override
    public void onEnable() {
        fast = false;
    }

    protected boolean badStack(ItemStack stack) {
        return badStack(stack, true, Collections.emptySet());
    }

    protected boolean badStack(ItemStack stack, boolean checkType, Set<PotionType> cycled) {
        PotionType type = PotionUtils.getPotionFromItem(stack);
        if (stack.getItem() instanceof ItemSpectralArrow) {
            type = SPECTRAL;
        }

        if (cycled.contains(type)) {
            return true;
        }

        if (checkType) {
            if (BAD_TYPES.contains(type)) {
                return true;
            }
        } else if (type.getEffects().isEmpty() && isValid("none")) {
            return false;
        }

        if (stack.getItem() instanceof ItemSpectralArrow) {
            return !isValid("Spectral") || mc.player.isGlowing();
        }

        boolean inValid = true;
        for (PotionEffect e : type.getEffects()) {
            if (!isValid(I18n.format(e.getPotion().getName()))) {
                return true;
            }

            PotionEffect eff = mc.player.getActivePotionEffect(e.getPotion());
            if (eff == null || eff.getDuration() < 200) {
                inValid = false;
            }
        }

        if (!checkType) {
            return false;
        }

        return inValid;
    }

    protected void cycle(boolean recursive, boolean key) {
        if (!InventoryUtil.validScreen()
                || key && !cycleTimer.passed(cycleDelay.getValue())) {
            return;
        }

        int firstSlot = -1;
        int secondSlot = -1;
        ItemStack arrow = null;
        if (isArrow(mc.player.getHeldItem(EnumHand.OFF_HAND))) {
            firstSlot = 45;
        }

        if (isArrow(mc.player.getHeldItem(EnumHand.MAIN_HAND))) {
            if (firstSlot == -1) {
                firstSlot = ItemUtil.hotbarToInventory(mc.player.inventory.currentItem);
            } else if (!badStack(mc.player.getHeldItem(EnumHand.MAIN_HAND), key, cycled)) {
                secondSlot = ItemUtil.hotbarToInventory(mc.player.inventory.currentItem);
                arrow = mc.player.getHeldItem(EnumHand.MAIN_HAND);
            }
        }

        if (!badStack(mc.player.inventory.getItemStack(), key, cycled)) {
            secondSlot = -2;
            arrow = mc.player.inventory.getItemStack();
        }

        if (firstSlot == -1 || secondSlot == -1) {
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (!isArrow(stack)) {
                    continue;
                }

                if (firstSlot == -1) {
                    firstSlot = ItemUtil.hotbarToInventory(i);
                } else if (!badStack(stack, key, cycled)) {
                    secondSlot = ItemUtil.hotbarToInventory(i);
                    arrow = stack;
                    break;
                }
            }
        }

        if (firstSlot == -1) {
            return;
        }

        if (secondSlot == -1) {
            if (!recursive && !cycled.isEmpty()) {
                cycled.clear();
                cycle(true, key);
            }

            return;
        }

        PotionType type = PotionUtils.getPotionFromItem(arrow);
        if (arrow.getItem() instanceof ItemSpectralArrow) {
            type = SPECTRAL;

        }

        cycled.add(type);
        int finalFirstSlot = firstSlot;
        int finalSecondSlot = secondSlot;
        Item inFirst = InventoryUtil.get(finalFirstSlot).getItem();
        Item inSecond = InventoryUtil.get(finalSecondSlot).getItem();
        if (InventoryUtil.get(finalFirstSlot).getItem() == inFirst && InventoryUtil.get(finalSecondSlot).getItem() == inSecond) {
            if (finalSecondSlot == -2) {
                InventoryUtil.click(finalFirstSlot);
            } else {
                InventoryUtil.click(finalSecondSlot);
                InventoryUtil.click(finalFirstSlot);
                InventoryUtil.click(finalSecondSlot);
            }
        }

        cycleTimer.reset();
    }

    protected ItemStack findArrow() {
        if (isArrow(mc.player.getHeldItem(EnumHand.OFF_HAND))) {
            return mc.player.getHeldItem(EnumHand.OFF_HAND);
        } else if (isArrow(mc.player.getHeldItem(EnumHand.MAIN_HAND))) {
            return mc.player.getHeldItem(EnumHand.MAIN_HAND);
        }

        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (isArrow(stack)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    private boolean isValid(String string) {
        if (string == null) {
            return false;
        }

        return !arStrings.contains(TextUtil.formatString(string));
    }

    public Collection<String> getList() {
        return arStrings;
    }

    protected boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }
}
