package me.chachoox.lithium.impl.modules.other.font;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.impl.managers.Managers;

import java.awt.*;

public class CustomFont extends Module {

    public final StringProperty font =
            new StringProperty(
                    "Verdana",
                    new String[]{"Font", "f"}
            );

    public final NumberProperty<Integer> size =
            new NumberProperty<>(
                    18, 12, 24,
                    new String[]{"Size", "sz"},
                    "The size of the font."
            );

    public final NumberProperty<Integer> style =
            new NumberProperty<>(
                    0, 0, 3,
                    new String[]{"Style", "styl"},
                    "1- Bold /" + " 2- Italics / " + "3- Bold and Italics"
            );

    public final Property<Boolean> fractionalMetrics =
            new Property<>(
                    true,
                    new String[]{"FractionalMetrics", "fracmetric"},
                    "Takes sub pixel accuracy into account."
            );

    public final Property<Boolean> antiAlias =
            new Property<>(
                    true,
                    new String[]{"AntiAlias", "alias"},
                    "Makes the edges of the font smoother."
            );

    public final Property<Boolean> shadow =
            new Property<>(
                    true,
                    new String[]{"Shadow", "shad"},
                    "Tweaks the shadows in the font."
            );

    public final Property<Boolean> chat =
            new Property<>(
                    false,
                    new String[]{"Chat", "c"},
                    "Replaces chat's font."
            );

    public final Property<Boolean> full =
            new Property<>(
                    false,
                    new String[]{"Full", "ful"}, "Replaces minecraft font everywhere."
            );

    public CustomFont() {
        super("Font", new String[]{"Font", "customfont", "fontchanger"}, "Modifies the client font.", Category.OTHER);
        this.offerProperties(font, size, style, fractionalMetrics, antiAlias, shadow, chat, full);
        registerObservers();
    }

    private void registerObservers() {
        for (Property<?> property : this.getProperties()) {
            if (!property.equals(full)) {
                property.addObserver(e -> Managers.FONT.setFontRenderer());
            }
        }

    }

    @Override
    public void onLoad() {
        Managers.FONT.setFontRenderer();
    }

    @Override
    public void onEnable() {
        Managers.FONT.cFont = true;
    }

    @Override
    public void onDisable() {
        Managers.FONT.cFont = false;
    }

    public Font getFont() {
        //noinspection MagicConstant
        return new Font(font.getValue(), style.getValue(), size.getValue());
    }

    public boolean isChat() {
        return isEnabled() && chat.getValue();
    }

    public boolean isFull() {
        return isEnabled() && full.getValue() && mc.world != null;
    }

}
