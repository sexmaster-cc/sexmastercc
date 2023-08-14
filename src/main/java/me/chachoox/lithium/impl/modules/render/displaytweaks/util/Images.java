package me.chachoox.lithium.impl.modules.render.displaytweaks.util;

public enum Images {
    PIG("lithium/textures/screen/pig.png"), //got this from swift dont ask
    BLUE_NEBULA("lithium/textures/screen/blue_nebula.png"),
    PURPLE("lithium/textures/screen/purple.png"),
    RED_BLACK("lithium/textures/screen/red_black.png"),
    BANDHU("lithium/textures/screen/aetra_gang.png");

    final String image;

    Images(String image) {
        this.image = image;
    }

    public String get() {
        return this.image;
    }
}
