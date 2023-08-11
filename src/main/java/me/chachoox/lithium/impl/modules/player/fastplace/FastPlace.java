package me.chachoox.lithium.impl.modules.player.fastplace;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.util.list.ListEnum;
import net.minecraft.item.Item;

import java.util.Collection;

public class FastPlace extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.ANY,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    1, 0, 4,
                    new String[]{"Delay", "del", "d"},
                    "What we set the rightclickdelay timer to."
            );

    protected final Property<Boolean> boatFix =
            new Property<>(
                    false,
                    new String[]{"BoatFix", "2b2tthing", "boats"},
                    "Fixes servers that try to patch placing boats."
            );

    protected final ItemList itemList = new ItemList(ListEnum.ITEM_LIST_ALIAS);

    public FastPlace() {
        super("FastPlace", new String[]{"FastPlace", "FastUse", "InstantUse", "InstantPlace"}, "Changes right click delay when holding a valid item.", Category.PLAYER);
        this.offerListeners(new ListenerPlaceBoat(this), new ListenerTick(this));
        this.offerProperties(whitelist, delay, boatFix, itemList);
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
