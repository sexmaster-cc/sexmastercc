package me.chachoox.lithium.impl.modules.render.fullbright;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.Property;

import java.awt.*;

public class Fullbright extends Module {

    protected final Property<Boolean> lightMap =
            new Property<>(
                    false,
                    new String[]{"Colored", "coloured", "lightmap", "ambience"},
                    "Changes the minecraft lightmap color."
            );

    protected final ColorProperty lightMapColor =
            new ColorProperty(
                    new Color(85, 75, 255, 125),
                    false,
                    new String[]{"Color", "colour", "lightmapcolor"}
            );

    protected final Property<Boolean> bozeMode =
            new Property<>(false,
                    new String[]{"BozeAmbience", "gay"},
                    "yea."
            );

    public Fullbright() {
        super("FullBright", new String[]{"Fullbright", "Fullbrightness", "Gamma", "Lightness", "Ambience", "Ambient", "Brightness"}, "Modifies game brightness and the lightmap.", Category.RENDER);
        this.offerProperties(lightMap, lightMapColor, bozeMode);
        this.offerListeners(new ListenerRender(this));
    }

    @Override
    public void onEnable() {
        mc.gameSettings.gammaSetting = 100;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = 0.5f;
    }

    public boolean customColor() {
        return lightMap.getValue() && !bozeMode.getValue();
    }

    public Color getColor() {
        return lightMapColor.getColor();
    }
}
