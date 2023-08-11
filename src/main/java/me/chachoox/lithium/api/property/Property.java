package me.chachoox.lithium.api.property;

import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.observable.Observable;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.property.list.ItemList;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.util.PropertyEvent;

import java.awt.*;
import java.util.List;

public class Property<T> extends Observable<PropertyEvent<T>> implements Labeled {
    private final String[] aliases;
    private String description;
    protected T value;
    protected T initial;

    public Property(T initialValue, String[] aliases, String description) {
        this.value = initialValue;
        this.aliases = aliases;
        this.description = description;
    }

    public Property(T initialValue, String[] aliases) {
        this.value = initialValue;
        this.aliases = aliases;
    }

    @Override
    public String getLabel() {
        return aliases[0];
    }

    public String[] getAliases() {
        return aliases;
    }

    public T getValue() {
        return this.value;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setValue(T value) {
        PropertyEvent<T> event = onChange(new PropertyEvent<>(this, value));
        if (!event.isCanceled()) {
            this.value = event.getValue();
        } else {
            this.value = value;
        }
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public String getType() {
        if (this.isEnumProperty()) {
            return "Enum";
        }

        if (this instanceof ItemList) {
            return "ItemList";
        } else if (this instanceof BlockList) {
            return "BlockList";
        }

        return this.getClassName(this.value);
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public boolean isNumberProperty() {
        return (value instanceof Double || value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Float);
    }

    public boolean isEnumProperty() {
        return !isNumberProperty()
                && !(value instanceof Bind)
                && !(value instanceof String)
                && !(value instanceof Character)
                && !(value instanceof Boolean)
                && !(value instanceof Color)
                && !(value instanceof List);
    }

    public void reset() {
        value = initial;
    }

    public T getInitial() {
        return initial;
    }

    public boolean isStringProperty() {
        return value instanceof String;
    }
}
