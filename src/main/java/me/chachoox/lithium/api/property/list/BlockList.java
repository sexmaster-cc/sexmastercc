package me.chachoox.lithium.api.property.list;

import net.minecraft.block.Block;

import java.util.List;

public class BlockList extends ListProperty<Block> {
    public BlockList(List<Block> list, String[] aliases) {
        super(list, aliases);
    }

    public BlockList(String[] aliases) {
        super(aliases);
    }
}