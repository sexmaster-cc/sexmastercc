package me.chachoox.lithium.impl.modules.movement.icespeed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.init.Blocks;

/**
 * @author moneymaker552
 */
@SuppressWarnings("deprecation")
public class IceSpeed extends Module {

    protected final NumberProperty<Float> iceSpeed =
            new NumberProperty<>(
                    0.6f, 0.1f, 5.0f, 0.01f,
                    new String[]{"Slipperiness", "Speed", "Factor", "Sped", "slip", "slilppnyhik6febe.onion"},
                    "What we will set the blocks slipperiness to."
            );

    public IceSpeed() {
        super("IceSpeed", new String[]{"IceSpeed", "FastIce", "Ic", "Fi"}, "Modifies ice slipperiness.", Category.MOVEMENT);
        this.offerProperties(iceSpeed);
        this.offerListeners(new LambdaListener<>(UpdateEvent.class, event -> {
            Blocks.ICE.slipperiness = (this.iceSpeed.getValue());
            Blocks.FROSTED_ICE.slipperiness = (this.iceSpeed.getValue());
            Blocks.PACKED_ICE.slipperiness = (this.iceSpeed.getValue());
        }));
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = (0.98F);
        Blocks.FROSTED_ICE.slipperiness = (0.98F);
        Blocks.PACKED_ICE.slipperiness = (0.98F);
    }
}
