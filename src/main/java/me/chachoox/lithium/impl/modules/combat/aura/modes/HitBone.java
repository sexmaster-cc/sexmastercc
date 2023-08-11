package me.chachoox.lithium.impl.modules.combat.aura.modes;

public enum HitBone {
    FEET(0.0F),
    LEG(0.03F),
    DICK(0.6f),
    CHEST(0.7F),
    NECK(0.8F),
    HEAD(1.0F);

    private final float height;

    HitBone(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }
}
