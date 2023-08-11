package me.chachoox.lithium.impl.modules.misc.antiinteract;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.list.ListEnum;
import net.minecraft.block.Block;

import java.util.Collection;

public class AntiInteract extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.WHITELIST,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION
            );

    protected final Property<Boolean> sneak =
            new Property<>(
                    true,
                    new String[]{"Sneak", "Nosneak", "snk"},
                    "Wont try to cancel interact event if we are sneaking."
            );

    protected final Property<Boolean> onlyFood =
            new Property<>(
                    false,
                    new String[]{"Food", "onlyOnFood", "noFood", "foodOnly"},
                    "Only activate anti interact when holding food."
            );

    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);

    public AntiInteract() {
        super("AntiInteract", new String[]{"AntiInteract", "NoInteract"}, "Cancels right clicking on blocks", Category.MISC);
        this.offerProperties(whitelist, sneak, onlyFood, blockList);
        this.offerListeners(new ListenerInteract(this));
    }

    protected boolean isValid(Block block) {
        if (block == null) {
            return false;
        }

        if (whitelist.getValue() == ListEnum.ANY) {
            return true;
        }

        if (whitelist.getValue() == ListEnum.WHITELIST) {
            return getList().contains(block);
        }

        return !getList().contains(block);
    }

    public Collection<Block> getList() {
        return blockList.getValue();
    }

}
