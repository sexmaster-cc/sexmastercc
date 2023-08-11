package me.chachoox.lithium.impl.event.events.blocks;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.math.BlockPos;

public class BreakBlockEvent extends Event {
    private final BlockPos pos;

    public BreakBlockEvent(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}
