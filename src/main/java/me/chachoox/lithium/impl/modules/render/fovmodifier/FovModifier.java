package me.chachoox.lithium.impl.modules.render.fovmodifier;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;

public class FovModifier extends Module {

    private final NumberProperty<Float> flying = new NumberProperty<>(
            1.0f, 0.0f, 1.0f, 0.01f,
            new String[]{"Fly", "flight", "Flying"},
            "Fov modifier for when we are flying."
    );

    private final NumberProperty<Float> sprinting = new NumberProperty<>(
            0.4f, 0.0f, 1.0f, 0.01f,
            new String[]{"Sprint", "Sprinting", "Running"},
            "Fov modifier for when we are sprinting."
    );

    private final NumberProperty<Float> slow = new NumberProperty<>(
            0.0f, 0.0f, 1.0f, 0.01f,
            new String[]{"Slowness", "Slow", "Slowed"},
            "Fov modifier for when we are slowed."
    );

    private final NumberProperty<Float> swiftness = new NumberProperty<>(
            0.2f, 0.0f, 1.0f, 0.01f,
            new String[]{"Swiftness", "Speed", "sped"},
            "Fov modifier for when we have speed."
    );

    private final NumberProperty<Float> aim = new NumberProperty<>(
            0.2f, 0.0f, 1.0f, 0.01f,
            new String[]{"Aim", "Scoped", "Aimed"},
            "Fov modifier for when we are aiming."
    );

    private static FovModifier FOV_MODIFIER;

    public FovModifier() {
        super("FovModifier", new String[]{"FovModifier", "FovMod", "FovChanger", "BetterFov"}, "Modifies dynamic fov.", Category.RENDER);
        this.offerProperties(flying, sprinting, slow, swiftness, aim);
        FOV_MODIFIER = this;
    }

    public static FovModifier get() {
        return (FOV_MODIFIER == null) ? (FOV_MODIFIER = new FovModifier()) : FOV_MODIFIER;
    }

    public float fly() {
        return flying.getValue();
    }

    public float sprint() {
        return sprinting.getValue();
    }

    public float slow() {
        return slow.getValue();
    }

    public float swiftness() {
        return swiftness.getValue();
    }

    public float aim() {
        return aim.getValue();
    }
}
