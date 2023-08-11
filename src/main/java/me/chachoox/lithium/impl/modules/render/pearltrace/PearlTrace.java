package me.chachoox.lithium.impl.modules.render.pearltrace;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.render.pearltrace.util.ThrownEntity;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class PearlTrace extends Module {

    protected final NumberProperty<Float> width =
            new NumberProperty<>(
                    1.0F, 1.0F, 4.0F, 0.1F,
                    new String[]{"Width", "width", "linewidth"},
                    "Thickness of the line."
            );

    protected final NumberProperty<Integer> timeout =
            new NumberProperty<>(
                    5000, 250, 10000,
                    new String[]{"Timeout", "time"},
                    "How many milliseconds each trail will last."
            );

    protected final ColorProperty color =
            new ColorProperty(
                    new Color(-1),
                    false,
                    new String[]{"Color", "colour"}
            );

    protected long time;

    protected final ConcurrentHashMap<Integer, ThrownEntity> thrownEntities = new ConcurrentHashMap<>();

    public PearlTrace() {
        super("PearlTrace", new String[]{"PearlTrace", "trails", "pearls"}, "Draws trails to pearls.", Category.RENDER);
        this.offerProperties(width, timeout, color);
        this.offerListeners(new ListenerRender(this), new ListenerUpdate(this));
    }

    protected Color getColor() {
        return color.getColor();
    }
}