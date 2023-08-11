package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;

/**
 * @author moneymaker552
 */
public class Velocity extends Module {

    protected final NumberProperty<Integer> horizontal =
            new NumberProperty<>(
                    0, 0, 100,
                    new String[]{"Horizontal", "horz"},
                    "How much kb we want to take horizontally."
            );

    protected final NumberProperty<Integer> vertical =
            new NumberProperty<>(
                    0, 0, 100,
                    new String[]{"Vertical", "vert"},
                    "How much kb we want to take veritcally."
            );

    protected final Property<Boolean> noPush =
            new Property<>(
                    true,
                    new String[]{"NoPush", "AntiPush", "PushPrevent"},
                    "Stops us from getting pushed by players & blocks."
            );

    protected final Property<Boolean> fishingRod =
            new Property<>(
                    true,
                    new String[]{"FishingRod", "Rods", "Fishingrods", "noFishingRodPull"},
                    "Stops us from getting pulled by fishing rods."
            );

    public Velocity() {
        super("Velocity", new String[]{"Velocity", "Velo", "AntiKnockback", "AntiKB", "NoKnockback"}, "Modifies player knockback.", Category.MOVEMENT);
        this.offerProperties(horizontal, vertical, noPush, fishingRod);
        this.offerListeners(new ListenerVelocity(this), new ListenerExplosion(this), new ListenerBlockPush(this), new ListenerStatus(this));
    }

    @Override
    public String getSuffix() {
        return "H" + horizontal.getValue() + "%V" + vertical.getValue() + "%";
    }

    public Boolean doNoPush() {
        return isEnabled() && noPush.getValue();
    }

    public void setVelocity(int h, int v) {
        horizontal.setValue(h);
        vertical.setValue(v);
    }
}
