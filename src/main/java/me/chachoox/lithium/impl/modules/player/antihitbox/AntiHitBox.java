package me.chachoox.lithium.impl.modules.player.antihitbox;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.list.ListEnum;
import net.minecraft.item.Item;

import java.util.Collection;

public class AntiHitBox extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.WHITELIST,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION
            );

    protected final ItemList itemList = new ItemList(ListEnum.ITEM_LIST_ALIAS);

    protected boolean noTrace;

    public AntiHitBox() {
        super("AntiHitbox", new String[]{"AntiHitbox", "NoHitBox", "NoHitBoxBlock"}, "Cancels hitbox if we are holding a valid item.", Category.PLAYER);
        this.offerProperties(whitelist, itemList);
        this.offerListeners(new ListenerUpdate(this));
    }

    public boolean canTrace() {
        return isEnabled() && noTrace;
    }

    protected boolean isValid(Item item) {
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
