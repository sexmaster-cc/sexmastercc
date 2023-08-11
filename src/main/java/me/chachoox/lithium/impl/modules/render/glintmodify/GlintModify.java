package me.chachoox.lithium.impl.modules.render.glintmodify;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

import java.awt.*;

public class GlintModify extends Module {

    private final Property<Boolean> armor =
            new Property<>(
                    true,
                    new String[]{"Armor", "armour"},
                    "Modifies enchantment glint on armor."
            );

    private final ColorProperty armorGlint =
            new ColorProperty(
                    new Color(0, 0, 255),
                    false,
                    new String[]{"ArmorColor", "armourColor", "ArmorColour", "ArmourColour"}
            );

    private final ColorProperty itemGlint =
            new ColorProperty(
                    new Color(0, 0, 255),
                    false,
                    new String[]{"ItemColor", "ItemColour"}
            );

    protected final NumberProperty<Float> glintScale =
            new NumberProperty<>(
                    8.0f, 0.0f, 20.0f, 0.1f,
                    new String[]{"GlintScale", "scale", "gs"},
                    "The scale of the glint."
            );

    protected final NumberProperty<Float> glintMult =
            new NumberProperty<>(
                    1.0f, 0.1f, 10.0f, 0.1f,
                    new String[]{"GlintMult", "Glintmultiplier", "gm"},
                    "The multipler of the glint."
            );

    protected final NumberProperty<Float> glintRotate =
            new NumberProperty<>(
                    1.0f, 0.1f, 10.0f, 0.1f,
                    new String[]{"GlintRotate", "glintrot", "gr"},
                    "The rotations of the glint."
            );

    public GlintModify() {
        super("GlintModify", new String[]{"GlintModify", "EnchantColor", "EnchantTweaks", "GlintEdit"}, "Modifies minecrafts enchanting glint.", Category.RENDER);
        this.offerProperties(armor, armorGlint, itemGlint, glintScale, glintMult, glintRotate);
    }

    public Color getEnchantColor() {
        return itemGlint.getColor();
    }

    public Color getArmorGlintColor() {
        return armorGlint.getColor();
    }

    public boolean isArmorGlint() {
        return isEnabled() && armor.getValue();
    }

    public float getGlintScale() {
        return glintScale.getValue();
    }

    public float getFactor() {
        return glintMult.getValue();
    }

    public float getGlintRotate() {
        return glintRotate.getValue();
    }
}
