package me.chachoox.lithium.impl.modules.render.tracers;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.modules.render.tracers.enums.Bone;

public class Tracers extends Module {

    protected final NumberProperty<Float> opacity =
            new NumberProperty<>(
                    0.5f, 0.0f, 1.0f, 0.01f,
                    new String[]{"Opacity", "traceropac", "traceralpha"},
                    "The opacity of the tracer."
            );

    protected final NumberProperty<Integer> ydistance =
            new NumberProperty<>(
                    70, 0, 255,
                    new String[]{"YDistance", "YLevel", "y"},
                    "Only renders tracers on players who are below this Y level."
            );

    protected final EnumProperty<Bone> bone =
            new EnumProperty<>(
                    Bone.FEET,
                    new String[]{"Bone", "bodypart", "target", "part"},
                    "What part of the body to draw tracers to."
            );

    public Tracers() {
        super("Tracers", new String[]{"Tracers", "trace", "tracer", "lines"}, "Draws lines to nearby players.", Category.RENDER);
        this.offerProperties(opacity, ydistance, bone);
        this.offerListeners(new ListenerRender(this));
    }
}
