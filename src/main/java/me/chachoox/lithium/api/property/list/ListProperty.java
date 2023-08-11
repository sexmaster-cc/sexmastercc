package me.chachoox.lithium.api.property.list;

import me.chachoox.lithium.api.property.Property;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ListProperty<T> extends Property<List<T>> {
    public ListProperty(List<T> list, String[] aliases) {
        super(list, aliases);
    }

    public ListProperty(String[] aliases) {
        super((List<T>) new ArrayList(), aliases);
    }
}