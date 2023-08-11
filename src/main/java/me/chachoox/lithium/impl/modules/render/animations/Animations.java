package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

public class Animations extends Module {

    protected final EnumProperty<SwingEnum> swing =
            new EnumProperty<>(
                    SwingEnum.NONE,
                    new String[]{"Swing", "swin"},
                    "Type of swinging."
            );

    protected final Property<Boolean> oldSwing =
            new Property<>(
                    false,
                    new String[]{"OldSwing", "olswin"},
                    "Swinging animation from 1.8."
            );

    protected final NumberProperty<Double> animationSpeed =
            new NumberProperty<>(27D, 0D, 70D, 1D,
                    new String[]{"AnimationSpeed", "animspeed"},
                    "Delay of the eat animation"
            );

    protected final NumberProperty<Float> eatingSpeed =
            new NumberProperty<>(4.0F, 0.0F, 40.0F, 1.0F,
                    new String[]{"EatingSpeed", "EatSpeed"},
                    "How fast u gonna eat fat nigga."
            );

    protected final NumberProperty<Float> eatHeight =
            new NumberProperty<>(0.1F, 0.0F, 1.0F, 0.1F,
                    new String[]{"Vertical", "ver"},
                    "Height of the eating animation"
            );

    public Animations() {
        super("Animations", new String[]{"Animations", "anim", "swing"}, "Modifies certain animations", Category.RENDER);
        this.offerProperties(swing, oldSwing, animationSpeed, eatingSpeed, eatHeight);
        this.offerListeners(new ListenerUpdate(this), new ListenerSwing(this));
    }

    public boolean isOldSwing() {
        return isEnabled() && oldSwing.getValue();
    }

    public double getAnimationSpeed() {
        return animationSpeed.getValue();
    }

    public float getEatingSpeed() {
        return eatingSpeed.getValue();
    }

    public float getEatHeight() {
        return eatHeight.getValue();
    }
}
