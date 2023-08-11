package me.chachoox.lithium.api.property;

import me.chachoox.lithium.impl.modules.other.colours.Colours;

import java.awt.*;

public class ColorProperty extends Property<Color> {
    private boolean global;

    public ColorProperty(Color color, boolean global, String[] aliases) {
        super(color, aliases);
        this.value = color;
        this.global = global;
    }

    public Color getColor() {
        if (isGlobal()) {
            return Colours.get().getColourCustomAlpha(value.getAlpha());
        }
        return value;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }
}
