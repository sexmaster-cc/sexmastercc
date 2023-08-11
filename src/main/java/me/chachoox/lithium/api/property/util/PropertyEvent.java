package me.chachoox.lithium.api.property.util;

import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.api.property.Property;

public class PropertyEvent<T> extends Event {
    private final Property<T> property;
    private T value;

    public PropertyEvent(Property<T> property, T value) {
        this.property = property;
        this.value = value;
    }

    public Property<T> getProperty() {
        return property;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}

