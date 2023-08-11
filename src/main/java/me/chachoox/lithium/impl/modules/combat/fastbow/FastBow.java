package me.chachoox.lithium.impl.modules.combat.fastbow;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;

/**
 * @author moneymaker552
 */
public class FastBow extends Module {

    protected final NumberProperty<Integer> ticks =
            new NumberProperty<>(
                    10, 3, 20,
                    new String[]{"Ticks", "Tickington", "time", "releasetime"},
                    "How many ticks have to of passed since we started charging the bow to release."
            );

    public FastBow() {
        super("FastBow", new String[]{"Fastbow", "autobowrelease", "bowspam"}, "Automatically release bow once delay passed.", Category.COMBAT);
        this.offerProperties(ticks);
        this.offerListeners(new ListenerTick(this));
    }
}
