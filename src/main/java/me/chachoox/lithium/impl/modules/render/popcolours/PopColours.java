package me.chachoox.lithium.impl.modules.render.popcolours;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;

import java.awt.*;

public class PopColours extends Module {

    private final NumberProperty<Float> scale =
            new NumberProperty<>(
                    0.75F, 0.0F, 1.0F, 0.01F,
                    new String[]{"Scale", "scal"},
                    "The scale of the totem pop particles."
            );

    private final ColorProperty color =
            new ColorProperty(
                    Color.GREEN,
                    false,
                    new String[]{"FirstColor", "firstcolour"}
            );

    private final ColorProperty secondColor =
            new ColorProperty(
                    Color.YELLOW,
                    false,
                    new String[]{"SecondColor", "secondcolour"}
            );

    private final NumberProperty<Float> randomizeRed =
            new NumberProperty<>(
                    0.0F, 0.0F, 1.0F, 0.1f,
                    new String[]{"FactorRed", "facred"},
                    "How much we want to randomize the red on the pops."
            );

    private final NumberProperty<Float> randomizeGreen =
            new NumberProperty<>(
                    0.0F, 0.0F, 1.0F, 0.1f,
                    new String[]{"FactorGreen", "facgreen"},
                    "How much we want to randomize the blue on the pops."
            );

    private final NumberProperty<Float> randomizeBlue =
            new NumberProperty<>(
                    0.0F, 0.0F, 1.0F, 0.1f,
                    new String[]{"FactorBlue", "facblue"},
                    "How much we want to randomize the blue on the pops."
            );

    public PopColours() {
        super("PopColours", new String[]{"PopColours", "popcolor", "totempopcolor"}, "Changes the color of totem pop particles.", Category.RENDER);
        this.offerProperties(scale, color, secondColor, randomizeRed, randomizeGreen, randomizeBlue);
    }

    public Color getColor() {
        return color.getColor();
    }

    public Color getSecondColor() {
        return secondColor.getColor();
    }

    public Float getScale() {
        return scale.getValue();
    }

    public Float getRandomRed() {
        return randomizeRed.getValue();
    }

    public Float getRandomBlue() {
        return randomizeBlue.getValue();
    }

    public Float getRandomGreen() {
        return randomizeGreen.getValue();
    }
}
