package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

public class Fly extends Module {

    protected final NumberProperty<Double> speed =
            new NumberProperty<>(
                    2.5, 0.1, 10.0, 0.5,
                    new String[]{"Speed", "sped"},
                    "How fast we move."
            );

    protected final Property<Boolean> autoDisable =
            new Property<>(
                    false,
                    new String[]{"AutoDisable", "lagback"},
                    "Automatically disables after we get lagged back."
            );

    public Fly() {
        super("Fly", new String[]{"Fly", "Flight"}, "Just fly.", Category.MOVEMENT);
        this.offerProperties(speed, autoDisable);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this), new ListenerPacket(this), new ListenerPosLook(this));
    }
}
