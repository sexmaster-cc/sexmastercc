package me.chachoox.lithium.impl.modules.other.hud.util;

import net.minecraft.item.ItemStack;

public class ItemHolder {
    private final ItemStack stack;
    private final int count;

    public ItemHolder(ItemStack stack, int count) {
        this.stack = stack;
        this.count = count;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getCount() {
        return count;
    }
}
