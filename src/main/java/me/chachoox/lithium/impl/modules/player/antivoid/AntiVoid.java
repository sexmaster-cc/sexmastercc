package me.chachoox.lithium.impl.modules.player.antivoid;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;

/**
 * @author moneymaker552
 */
public class AntiVoid extends Module {

    protected final EnumProperty<AntiVoidMode> mode =
            new EnumProperty<>(
                    AntiVoidMode.ANTI,
                    new String[]{"Mode", "type", "method"},
                    "Anti - Sets Y Motion to 1 to instantly lag you back, only works in Y Level -> (-5 - -10) " +
                            "/ Stop - Stops all Y motion if Y position is below 0."
            );

    public AntiVoid() {
        super("AntiVoid", new String[]{"AntiVoid", "NoVoid", "Avoid"}, "Prevents you from falling into the void.", Category.PLAYER);
        this.offerListeners(new ListenerTick(this));
        this.offerProperties(mode);
    }
}
