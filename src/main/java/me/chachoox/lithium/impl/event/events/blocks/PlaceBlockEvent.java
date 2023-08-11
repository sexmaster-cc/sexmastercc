package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockEvent extends Event {
    private final BlockPos pos;
    private final ItemStack stack;

    public PlaceBlockEvent(BlockPos pos, ItemStack stack) {
        this.pos = pos;
        this.stack = stack;
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }

}
