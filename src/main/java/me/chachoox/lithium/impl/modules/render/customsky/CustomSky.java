package me.chachoox.lithium.impl.modules.render.customsky;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.impl.modules.render.customsky.mode.Mode;

import java.awt.*;

public class CustomSky extends Module {

    public final EnumProperty<Mode> mode =
            new EnumProperty<>(Mode.SKY,
                    new String[]{"Mode", "Type", "Method"},
                    "Mode."
            );

    public final ColorProperty color = new ColorProperty(new Color(-1), true, new String[]{"SkyColor", "colour", "color"});

    public CustomSky() {
        super("CustomSky", new String[]{"CustomSky", "SkyTweaks", "FogColor", "SkyColor", "SkyColour", "FogColour"}, "Changes the sky and the fog colours.", Category.RENDER);
        this.offerProperties(mode, color);
    }

    public Color getSkyColor() {
        return color.getColor();
    }
}
