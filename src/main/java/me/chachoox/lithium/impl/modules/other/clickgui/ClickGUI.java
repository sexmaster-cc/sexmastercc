package me.chachoox.lithium.impl.modules.other.clickgui;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;

import java.awt.*;

public class ClickGUI extends Module {

    public boolean loaded = false;

    public final Property<Boolean> lowercaseModules =
            new Property<>(
                    false,
                    new String[]{"LowercaseModules", "lowercasem", "lcm"},
                    "Makes all the modules text lowercase."
            );

    public final Property<Boolean> lowercaseProperties =
            new Property<>(
                    true,
                    new String[]{"LowercaseProperties", "lowercasep", "lcp"},
                    "Makes all the properties text lowercase."
            );

    public final Property<Boolean> lowercaseDescriptions =
            new Property<>(
                    false,
                    new String[]{"LowercaseDescriptions", "lowercased", "lcd"},
                    "Makes all the descriptions text lowercase."
            );

    public final Property<Boolean> lowercaseCategories =
            new Property<>(
                    false,
                    new String[]{"LowercaseCategories", "Lowercasecategory", "lcc"},
                    "Makes all the category text lowercase."
            );

    public final Property<Boolean> lowercaseKeybinds =
            new Property<>(
                    true,
                    new String[]{"LowercaseKeybinds", "Lowercasekey", "lowercasek"},
                    "Makes all the keybinds text lowercase."
            );

    public final Property<Boolean> whiteResult =
            new Property<>(
                    true,
                    new String[]{"WhiteResult", "whitenumber", "whitebind", "whiteenum"},
                    "Makes the result (numbers, enums, keybinds) white instead of gray."
            );

    public final Property<Boolean> gradient =
            new Property<>(
                    false,
                    new String[]{"Gradient", "grad", "gradien", "g"},
                    "Draws a gradient fade when the gui is open."
            );

    public final Property<Boolean> sideWays =
            new Property<>(
                    false,
                    new String[]{"Sideways", "Sideway", "left"},
                    "Makes the gradient sideways instead of on the bottom."
            );

    public final Property<Boolean> aliMode =
            new Property<>(
                    false,
                    new String[]{"AliMode", "ali", "nigga", "che"},
                    "Nigga."
            );

    public final Property<Boolean> blur =
            new Property<>(
                    false,
                    new String[]{"Blur", "cum"},
                    "Minecraft's terrible blur."
            );

    public final ColorProperty categoryColor =
            new ColorProperty(
                    new Color(0x66FFFFFF, true),
                    true,
                    new String[]{"Category", "cat", "categorycolor", "catcolor"}
            );

    public final ColorProperty categoryText =
            new ColorProperty(
                    new Color(-1, true),
                    false,
                    new String[]{"CategoryText", "cattext"}
            );

    public final ColorProperty enabledButtonColor =
            new ColorProperty(
                    new Color(0x66FFFFFF, true),
                    true,
                    new String[]{"EnabledRect", "onrect"}
            );

    public final ColorProperty disabledButtonColor =
            new ColorProperty(
                    new Color(0x26FFFFFF, true),
                    true,
                    new String[]{"DisabledRect", "offrect"}
            );

    public final ColorProperty enabledTextColor =
            new ColorProperty(
                    new Color(-1),
                    false,
                    new String[]{"EnabledText", "ontext"}
            );

    public final ColorProperty disabledTextColor =
            new ColorProperty(
                    new Color(-5592406),
                    false,
                    new String[]{"DisabledText", "offtext"}
            );

    public final ColorProperty backgroundColor =
            new ColorProperty(
                    new Color(1996488704, true),
                    false,
                    new String[]{"Background", "bg"}
            );

    public final ColorProperty propertyColor =
            new ColorProperty(
                    new Color(0xCCFFFFFF, true),
                    true,
                    new String[]{"PropertyRect", "settingrect"}
            );

    public final ColorProperty descriptionOutline =
            new ColorProperty(
                    new Color(-1, true),
                    true,
                    new String[]{"DescriptionOutline", "descoutline"}
            );

    public final ColorProperty descriptionColor =
            new ColorProperty(
                    new Color(0, true),
                    false,
                    new String[]{"Description", "desc"}
            );

    public final ColorProperty descriptionText =
            new ColorProperty(
                    new Color(-1, true),
                    false,
                    new String[]{"DescriptionText", "desctext"}
            );

    public final ColorProperty gradientFirstColor =
            new ColorProperty(
                    new Color(30, 30, 30, 155),
                    false,
                    new String[]{"GradientOne", "gradone", "gradientwo"}
            );

    public final ColorProperty gradientSecondColor =
            new ColorProperty(
                    new Color(0, 0, 0, 155),
                    false,
                    new String[]{"GradientTwo", "gradtwo", "gradientwo"}
            );

    private static ClickGUI CLICK_GUI;

    public ClickGUI() {
        super("ClickGUI", new String[]{"ClickGUI", "gui", "clientgui"}, "Interface of the client.", Category.OTHER);
        this.offerProperties(lowercaseModules, lowercaseProperties, lowercaseDescriptions, lowercaseKeybinds, lowercaseCategories,
                whiteResult, gradient, sideWays, aliMode, blur, categoryColor, categoryText, enabledButtonColor, disabledButtonColor, enabledTextColor,
                disabledTextColor, backgroundColor, propertyColor, descriptionOutline, descriptionColor, descriptionText, gradientFirstColor, gradientSecondColor);
        this.offerListeners(new ListenerTick(this));
        CLICK_GUI = this;
    }

    public static ClickGUI get() {
        return CLICK_GUI;
    }

    public Color getEnabledButtonColor() {
        return enabledButtonColor.getColor();
    }

    public Color getDisabledButtonColor() {
        return disabledButtonColor.getColor();
    }

    public Color getEnabledTextColor() {
        return enabledTextColor.getColor();
    }

    public Color getDisabledTextColor() {
        return disabledTextColor.getColor();
    }

    public Color getBackgroundColor() {
        return backgroundColor.getColor();
    }

    public Color getPropertyColor() {
        return propertyColor.getColor();
    }

    public Color getDescriptionOutlineColor() {
        return descriptionOutline.getColor();
    }

    public Color getDescriptionColor() {
        return descriptionColor.getColor();
    }

    public Color getDescriptionTextColor() {
        return descriptionText.getColor();
    }

    public Color getCategoryColor() {
        return categoryColor.getColor();
    }

    public Color getCategoryTextColor() {
        return categoryText.getColor();
    }

    public String aliModule() {
        return aliMode.getValue() ? lowercaseModules.getValue() ? "nigga" : "Nigga" : "";
    }

    public String aliProperty() {
        return aliMode.getValue() ? lowercaseProperties.getValue() ? "nigga" : "Nigga" : "";
    }

    public String aliKeybind() {
        return aliMode.getValue() ? lowercaseKeybinds.getValue() ? "nigga key" : "Nigga Key" : "";
    }

    public String aliCategory() {
        return aliMode.getValue() ? lowercaseCategories.getValue() ? "nigga" : "Nigga" : "";
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.displayGuiScreen(new SexMasterGui());
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof SexMasterGui) {
            mc.displayGuiScreen(null);
        }
    }
}
