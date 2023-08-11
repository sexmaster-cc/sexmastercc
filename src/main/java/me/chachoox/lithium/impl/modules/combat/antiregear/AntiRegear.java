package me.chachoox.lithium.impl.modules.combat.antiregear;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;

public class AntiRegear extends Module {

    protected final Property<Boolean> rotation =
            new Property<>(false,
                    new String[]{"Rotation", "rotate"},
                    "Rotates to the current shulker."
            );

    protected final NumberProperty<Float> enemyRange =
            new NumberProperty<>(
                    8.0F, 1.0F, 15.0F, 1.0F,
                    new String[]{"EnemyRange", "enemyrang", "er"},
                    "How far an enemy has to be before it starts breaking shulkers."
            );

    protected final NumberProperty<Float> range =
            new NumberProperty<>(
                    5.0F, 1.0F, 6.0F, 0.1F,
                    new String[]{"Range", "rang", "r"},
                    "The range of how far you want to break shulkers from."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(150, 0, 250,
                    new String[]{"Delay", "d"},
                    "The delay between switching from one shulker to another."
            );

    protected final Property<Boolean> raytrace =
            new Property<>(false,
                    new String[]{"Raytrace", "ray"},
                    "Wont break shulkers through blocks."
            );

    protected final StopWatch timer = new StopWatch();

    public AntiRegear() {
        super("AntiRegear", new String[]{"AntiRegear", "noregear", "regear"}, "Breaks your opponent's shulkers.", Category.COMBAT);
        this.offerProperties(rotation, enemyRange, range, delay, raytrace);
        this.offerListeners(new ListenerMotion(this));
    }
}
