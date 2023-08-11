package me.chachoox.lithium.impl.modules.player.fastdrop;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.list.ListEnum;
import net.minecraft.item.Item;

import java.util.Collection;

/**
 * @author moneymaker552
 */
public class FastDrop extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.ANY,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION
            );

    private final ItemList itemList = new ItemList(ListEnum.ITEM_LIST_ALIAS);

    public FastDrop() {
        super("FastDrop", new String[]{"FastDrop", "InstantDrop"}, "Drops items faster when holding drop key.", Category.PLAYER);
        this.offerProperties(whitelist, itemList);
        this.offerListeners(new ListenerUpdate(this));
    }

    protected boolean isItemValid(Item item) {
        if (item == null) {
            return false;
        }

        if (whitelist.getValue() == ListEnum.ANY) {
            return true;
        }

        if (whitelist.getValue() == ListEnum.WHITELIST) {
            return getList().contains(item);
        }

        return !getList().contains(item);
    }

    public Collection<Item> getList() {
        return itemList.getValue();
    }
}
