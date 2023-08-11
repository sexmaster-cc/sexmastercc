package me.chachoox.lithium.api.util.inventory;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements Minecraftable {

    public static boolean isHolding(Item item) {
        return isHolding(mc.player, item);
    }

    public static boolean isHolding(EntityLivingBase entity, Item item) {
        ItemStack mainHand = entity.getHeldItemMainhand();
        ItemStack offHand  = entity.getHeldItemOffhand();
        return ItemUtil.areSame(mainHand, item) || ItemUtil.areSame(offHand, item);
    }

    public static void click(int slot) {
        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
    }

    public static ItemStack get(int slot) {
        if (slot == -2) {
            return mc.player.inventory.getItemStack();
        }

        return mc.player.inventoryContainer.getInventory().get(slot);
    }

    public static void put(int slot, ItemStack stack) {
        if (slot == -2) {
            mc.player.inventory.setItemStack(stack);
        }

        mc.player.inventoryContainer.putStackInSlot(slot, stack);
    }

    public static boolean validScreen() {
        return !(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof GuiInventory;
    }

    public static boolean equals(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null) {
            return stack2 == null;
        } else if (stack2 == null) {
            return false;
        }

        boolean empty1 = stack1.isEmpty();
        boolean empty2 = stack2.isEmpty();

        return empty1 == empty2
                && stack1.getDisplayName().equals(stack2.getDisplayName())
                && stack1.getItem() == stack1.getItem()
                && stack1.getHasSubtypes() == stack2.getHasSubtypes()
                && stack1.getMetadata() == stack2.getMetadata()
                && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static boolean canStack(ItemStack inSlot, ItemStack stack)
    {
        return inSlot.isEmpty()
                || inSlot.getItem() == stack.getItem()
                && inSlot.getMaxStackSize() > 1
                && (!inSlot.getHasSubtypes()
                || inSlot.getMetadata() == stack.getMetadata())
                && ItemStack.areItemStackTagsEqual(inSlot, stack);
    }
}
