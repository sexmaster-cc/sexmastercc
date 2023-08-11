package me.chachoox.lithium.api.util.text;

public enum ColorEnum {
    NONE(""),
    BLACK(TextColor.BLACK),
    WHITE(TextColor.WHITE),
    DARKBLUE(TextColor.DARK_BLUE),
    DARKGREEN(TextColor.DARK_GREEN),
    DARKAQUA(TextColor.DARK_AQUA),
    DARKRED(TextColor.DARK_RED),
    DARKPURPLE(TextColor.DARK_PURPLE),
    DARKGRAY(TextColor.DARK_GRAY),
    GRAY(TextColor.GRAY),
    GOLD(TextColor.GOLD),
    BLUE(TextColor.BLUE),
    GREEN(TextColor.GREEN),
    AQUA(TextColor.AQUA),
    RED(TextColor.RED),
    LIGHTPURPLE(TextColor.LIGHT_PURPLE),
    YELLOW(TextColor.YELLOW),
    OBFUSCATED(TextColor.OBFUSCATED),
    BOLD(TextColor.BOLD),
    STRIKE(TextColor.STRIKE),
    UNDERLINE(TextColor.UNDERLINE),
    ITALIC(TextColor.ITALIC),
    RESET(TextColor.RESET),
    RAINBOW(TextColor.RAINBOW);

    private final String color;

    ColorEnum(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static final String COLOR_DESC = "Current chat formatting color";
}
