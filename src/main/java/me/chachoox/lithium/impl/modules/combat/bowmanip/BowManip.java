package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;

/**
 * @author moneymaker552
 */
public class BowManip extends Module {

    protected final NumberProperty<Integer> spoofs =
            new NumberProperty<>(
                    150, 0, 300,
                    new String[]{"Spoofs", "SpoofTicks", "SpoofAmount"},
                    "How much we want to manipulate the bow, (how many packets, this sends 2 position packets per spoof, usually server limits packets to around 200)."
            );

    protected final NumberProperty<Float> ticks =
            new NumberProperty<>(
                    60.0f, 0.0f, 200.0f, 1.0f,
                    new String[]{"Ticks", "TickDelay", "Delay"},
                    "How long it takes to charge the " +
                            "(manipulated bow, this does not modify the actual minecraft bow, if you shoot a bow normally and this time hasn't passed it will shoot.) " +
                            "bow, time format -> (Minecraft Ticks)."
            );

    protected final NumberProperty<Float> fire =
            new NumberProperty<>(
                    60.0f, 0.0f, 200.0f, 1.0f,
                    new String[]{"Fire", "AutoFire", "ReleaseTime"},
                    "How long to we want to wait to AutoFire the bow, time format -> (Minecraft Ticks), set to 0-3 if you want this to not be used"
            );

    protected long last;
    protected StopWatch timer = new StopWatch();

    public BowManip() {
        super("BowManip", new String[]{"BowManip", "bowkiller", "fastarrows"}, "Manipulates bows to do more damage than normal.", Category.COMBAT);
        this.offerProperties(spoofs, ticks, fire);
        this.offerListeners(new ListenerDigging(this), new ListenerTick(this));
    }

    @Override
    public String getSuffix() {
        if (suffixCalculation() > ticks.getValue() * 50) {
            return ticks.getValue() * 50 + "";
        }
        return suffixCalculation() + "";
    }

    private double suffixCalculation() {
        return timer.getTime() - last + ticks.getValue() * 50 - ticks.getValue() * 50;
    }
}