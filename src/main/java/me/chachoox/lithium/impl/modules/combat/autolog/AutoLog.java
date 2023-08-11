package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;

/**
 * @author moneymaker552
 */
public class AutoLog extends Module {

    protected final Property<Boolean> onRender =
            new Property<>(
                    false,
                    new String[]{"OnRender", "VisualRange", "pussyassretardmode"},
                    "Logs whenever a player comes into render that isnt friended."
            );

    protected final NumberProperty<Float> healthCount =
            new NumberProperty<>(
                    12.0f, 1.0f, 19.0f, 0.5f,
                    new String[]{"HealthCount", "Hp", "LogHealth"},
                    "How low our health has to be to trigger autolog."
            );

    protected final NumberProperty<Integer> totemCount =
            new NumberProperty<>(
                    1, 0, 6,
                    new String[]{"TotemCount", "TotCount", "TotemAmount"},
                    "How many totems we have to have left to trigger autolog."
            );

    protected final Property<Boolean> fallDamage =
            new Property<>(
                    false,
                    new String[]{"FallDamage", "Fall", "AntiFallDamage"},
                    "Logs when we are about to die of fall damage."
            );

    protected final NumberProperty<Integer> threshold =
            new NumberProperty<>(
                    0, 0, 255,
                    new String[]{"Threshold", "Ylevel"},
                    "How low we have to be to logout."
            );

    public AutoLog() {
        super("AutoLog", new String[]{"autolog", "gaynigger", "grrr", "grrrrrr"}, "Automatically logs you out depending on your health and totems.", Category.COMBAT);
        this.offerProperties(onRender, healthCount, totemCount, fallDamage, threshold);
        this.offerListeners(new ListenerUpdate(this), new ListenerGui(this), new ListenerSpawn(this));
    }

    @Override
    public String getSuffix() {
        return healthCount.getValue().toString();
    }
}
