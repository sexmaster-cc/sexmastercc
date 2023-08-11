package me.chachoox.lithium.impl.modules.movement.phase;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.movement.phase.util.PhaseMode;

public class Phase extends Module {

    protected final NumberProperty<Integer> delay = new NumberProperty<>(1, 0, 30, new String[]{"Delay", "d"}, "Delay between packets being sent.");
    protected final EnumProperty<PhaseMode> mode = new EnumProperty<>(PhaseMode.ZOOM, new String[]{"Mode", "mod"}, "Type of phasing that will be used.");
    protected final Property<Boolean> slow = new Property<>(false, new String[]{"Slow", "slo"}, "Make motion slower.");

    protected StopWatch timer = new StopWatch();
    protected boolean zoomies = false;

    public Phase() {
        super("Phase", new String[]{"Phase", "testnoclip"}, "Test phase.", Category.MOVEMENT);
        this.offerProperties(delay, mode, slow);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this));
    }
}
