package me.chachoox.lithium.impl.modules.render.displaytweaks.util;

public enum Icons {
    CLASSIC(new String[]{"classic_16x16.png", "classic_32x32.png"}),
    SWIFT(new String[]{"swift_16x16.png", "swift_32x32.png"}),
    TUPAC(new String[]{"2pac_16x16.png", "2pac_32x32.png"}),
    SKULL(new String[]{"skull_16x16.png", "skull_32x32.png"}),
    LEAN(new String[]{"lean_16x16.png", "lean_32x32.png"});

    final String[] icons;

    Icons(String[] icons) {
        this.icons = icons;
    }

    public String[] get() {
        return this.icons;
    }
}
