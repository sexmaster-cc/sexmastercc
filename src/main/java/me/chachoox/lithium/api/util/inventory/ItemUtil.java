package me.chachoox.lithium.api.util.inventory;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import net.minecraft.block.Block;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.function.Predicate;

import static net.minecraft.item.Item.getItemFromBlock;

public class ItemUtil implements Minecraftable {

    public static void switchTo(int slot) {
        if (mc.player.inventory.currentItem != slot && slot > -1 && slot < 9) {
            mc.player.inventory.currentItem = slot;
            syncItem();
        }
    }

    public static void switchToAlt(int slot) {
        slot = hotbarToInventory(slot);
        if (mc.player.inventory.currentItem != slot && slot > 35 && slot < 45) {
            mc.playerController.windowClick(0, slot, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
        }
    }

    public static int findHotbarItem(Item item) {
        return findInHotbar(s -> ItemUtil.areSame(s, item));
    }

    public static boolean areSame(Item item1, Item item2) {
        return Item.getIdFromItem(item1) == Item.getIdFromItem(item2);
    }

    public static boolean isHolding(Class<?> clazz) {
        return clazz.isAssignableFrom(mc.player.getHeldItemMainhand().getItem().getClass()) || clazz.isAssignableFrom(mc.player.getHeldItemOffhand().getItem().getClass());
    }

    public static EnumHand getHand(int slot) {
        return slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    public static EnumHand getHand(Item item) {
        return mc.player.getHeldItemMainhand().getItem() == item ? EnumHand.MAIN_HAND : mc.player.getHeldItemOffhand().getItem() == item ? EnumHand.OFF_HAND : null;
    }

    public static int findInHotbar(Predicate<ItemStack> condition) {
        return findInHotbar(condition, true);
    }

    public static int findInHotbar(Predicate<ItemStack> condition, boolean offhand) {
        if (offhand && condition.test(mc.player.getHeldItemOffhand())) {
            return -2;
        }

        int result = -1;
        for (int i = 8; i > -1; i--) {
            if (condition.test(mc.player.inventory.getStackInSlot(i))) {
                result = i;
                if (mc.player.inventory.currentItem == i) {
                    break;
                }
            }
        }

        return result;
    }

    public static boolean areSame(ItemStack stack, Item item) {
        return stack != null && ItemUtil.areSame(stack.getItem(), item);
    }

    public static int getSlotHotbar(Item item) {
        int slot = -1;
        if (mc.player == null) {
            return slot;
        }

        for (int i = 8; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    public static void syncItem() {
        ((IPlayerControllerMP) mc.playerController).syncItem();
    }

    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
            }
        }

        return slot;
    }

    public static int getBlockFromHotbar(Block block) {
        int slot = -1;

        for (int i = 8; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == getItemFromBlock(block)) {
                slot = i;
            }
        }

        return slot;
    }

    public static int getItemSlot(Class<?> clss) {
        int itemSlot = -1;

        for (int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem().getClass() == clss) {
                itemSlot = i;
                break;
            }
        }

        return itemSlot;
    }

    public static int getItemSlot(Item item) {
        int itemSlot = -1;

        for (int i = 45; i > 0; i--) {
            if (mc.player.inventory.getStackInSlot(i).getItem().equals(item)) {
                itemSlot = i;
                break;
            }
        }

        return itemSlot;
    }

    public static int getItemCount(Item item) {
        int count = 0;

        int size = mc.player.inventory.mainInventory.size();
        for (int i = 0; i < size; i++) {
            final ItemStack itemStack = mc.player.inventory.mainInventory.get(i);
            if (itemStack.getItem() == item) {
                count += itemStack.getCount();
            }
        }

        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        if (offhandStack.getItem() == item) {
            count += offhandStack.getCount();
        }

        return count;
    }

    public static int hotbarToInventory(int slot) {
        if (slot == -2) {
            return 45;
        }

        if (slot > -1 && slot < 9) {
            return 36 + slot;
        }

        return slot;
    }

    public static double getDamageInPercent(ItemStack stack) {
        double percent = (double) stack.getItemDamage() / (double) stack.getMaxDamage();
        if (percent == 0.0) {
            return 100.0;
        } else if (percent == 1.0) {
            return 0.0;
        }

        return 100 - (percent * 100);
    }
}
