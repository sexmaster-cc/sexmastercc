package me.chachoox.lithium.impl.modules.combat.criticals;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;

/**
 * @author moneymaker552
 */
public class Criticals extends Module {

    protected final EnumProperty<CriticalsType> mode =
            new EnumProperty<>(
                    CriticalsType.PACKET,
                    new String[]{"Mode", "Type", "Method"},
                    "Packet: - Sends a normal critical packet every hit / " +
                            "Bypass - NCP with alternative offsets, might not bypass some NCP servers."
            );

    protected final Property<Boolean> yDifference =
            new Property<>(
                    false,
                    new String[]{"YDifference", "Ydiffer", "2b2tpvp", "strict"},
                    "Only uses criticals if we arent on the same Y level as the target."
            );

    public Criticals() {
        super("Criticals", new String[]{"criticals", "crit", "alwayscrit"}, "Always lands a critical hit.", Category.COMBAT);
        this.offerProperties(mode, yDifference);
        this.offerListeners(new ListenerUseEntity(this));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }
}
