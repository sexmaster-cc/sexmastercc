package me.chachoox.lithium.impl.modules.misc.timer;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;

public class Timer extends Module {

    public final NumberProperty<Float> speed =
            new NumberProperty<>(
                    1.0f, 0.1f, 10.0f, 0.1f,
                    new String[]{"Time", "speed", "sped"},
                    "What we want to change the tick speed to."
            );

    public Timer() {
        super("Timer", new String[]{"Timer", "time", "gamespeed"}, "Speeds up the game.", Category.MISC);
        this.offerProperties(speed);
    }

    @Override
    public String getSuffix() {
        return speed.getValue().toString();
    }
}
