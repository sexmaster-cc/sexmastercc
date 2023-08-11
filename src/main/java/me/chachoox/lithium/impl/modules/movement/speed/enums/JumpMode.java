package me.chachoox.lithium.impl.modules.movement.speed.enums;

public enum JumpMode {
    VANILLA(0.41999998688697815F),
    LOW(0.3999999463558197F);

    private final float height;

    JumpMode(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }
}
