package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.Items;

public class ListenerUpdate extends ModuleListener<AutoLog, UpdateEvent> {
    public ListenerUpdate(AutoLog module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        int yLevel = (int) MathUtil.round(mc.player.posY, 2);
        int math = module.threshold.getValue() - yLevel;

        String b;
        String a;

        if (math == 1) { //zenov code
            b = "block";
        }
        else {
            b = "blocks";
        }

        if (yLevel > module.threshold.getValue()) {
            a = " below ";
        } else {
            a = " above ";
        }

        int totems = 0;

        if (mc.player != null) {
            totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
        }

        if (mc.isSingleplayer()) {
            return;
        }

        String totemMessage =
                "Logged out with "
                        + MathUtil.round(mc.player.getHealth(), 2)
                        + " hearts and "
                        + totems
                        + " totems remaining";

        if (totems <= module.totemCount.getValue()
                && mc.player.getHealth() <= module.healthCount.getValue()) {
            NetworkUtil.disconnect(totemMessage);
            module.disable();
        }

        String fallDamageMessage =
                "Logged at Y level "
                        + yLevel
                        + " ("
                        + math
                        + ") "
                        + b
                        + a
                        + "set Y level with "
                        + totems
                        + " totems remaining";

        if (mc.player.posY <= module.threshold.getValue()
                && module.fallDamage.getValue()
                && totems <= module.totemCount.getValue()) {
            NetworkUtil.disconnect(fallDamageMessage);
            module.disable();
        }
    }
}
