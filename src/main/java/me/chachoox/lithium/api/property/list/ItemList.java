package me.chachoox.lithium.api.property.list;

import net.minecraft.item.Item;

import java.util.List;

public class ItemList extends ListProperty<Item> {
    public ItemList(List<Item> list, String[] aliases) {
        super(list, aliases);
    }

    public ItemList(String[] aliases) {
        super(aliases);
    }
}