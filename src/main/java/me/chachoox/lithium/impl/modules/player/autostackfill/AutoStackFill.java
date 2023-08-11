package me.chachoox.lithium.impl.modules.player.autostackfill;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoStackFill extends Module {

    protected final NumberProperty<Integer> threshold =
            new NumberProperty<>(
                    32, 1, 64,
                    new String[]{"Threshold", "thresh", "amount"},
                    "How low an item stack has to be for us to automatically refill it."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    1, 0, 10,
                    new String[]{"Delay", "del", "d"},
                    "Delay between refilling stacks."
            );

    protected final StopWatch timer = new StopWatch();

    public AutoStackFill() {
        super("AutoStackFill", new String[]{"Replenish", "AutoRefill", "Refill", "AutoStackFill", "HotbarRefill", "AutoFillStack"}, "Automatically fills item stacks.", Category.PLAYER);
        this.offerListeners(new ListenerGameLoop(this));
        this.offerProperties(threshold, delay);
    }

    protected void refillSlot(int slot) {
        ItemStack stack = mc.player.inventory.getStackInSlot(slot);
        if (stack.isEmpty() || stack.getCount() > threshold.getValue() || stack.getItem() == Items.AIR || !stack.isStackable() || stack.getCount() >= stack.getMaxStackSize()) {
            return;
        }

        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && canMergeWith(stack, itemStack)) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
            }
        }
    }

    private boolean canMergeWith(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem() && first.getDisplayName().equals(second.getDisplayName());
    }
}