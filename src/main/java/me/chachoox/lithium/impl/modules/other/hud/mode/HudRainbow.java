package me.chachoox.lithium.impl.modules.other.hud.mode;

import me.chachoox.lithium.api.util.text.TextColor;

public enum HudRainbow {
    HORIZONTAL(TextColor.RAINBOW_PLUS),
    VERTICAL(TextColor.RAINBOW_MINUS),
    DIAGONAL(TextColor.RAINBOW_EXTRA);

    private final String color;

    HudRainbow(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}

