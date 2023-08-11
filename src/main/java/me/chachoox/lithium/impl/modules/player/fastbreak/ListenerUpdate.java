package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.Blocks;

public class ListenerUpdate extends ModuleListener<FastBreak, UpdateEvent> {
    public ListenerUpdate(FastBreak module) {
        super(module, UpdateEvent.class, -10);
    }

    @Override
    public void call(UpdateEvent event) {
        if (mc.player.capabilities.isCreativeMode) {
            return;
        }

        if (module.pos != null) {
            if (mc.player.getDistanceSq(module.pos) > MathUtil.square(module.range.getValue())) {
                module.abortCurrentPos();
                return;
            }

            if (module.getBlock() != Blocks.AIR) {
                module.state = mc.world.getBlockState(module.pos);
            }

            module.updateDamages();

            int pickSlot = MineUtil.findBestTool(module.pos);

            if ((module.damages[mc.player.inventory.currentItem] >= 1.0f) || (pickSlot >= 0 && module.damages[pickSlot] >= 1.0f)) {
                module.canBreak = true;

                if (!module.pingTimer.passed(module.getPingDelay())) {
                    module.retryTimer.reset();
                    return;
                }

                if (!mc.player.onGround && module.strict.getValue()) {
                    module.retryTimer.reset();
                    return;
                }

                if (!module.auto.getValue() && !module.executed) {
                    return;
                }

                if (module.swap.getValue() == Swap.NONE && mc.player.inventory.currentItem != pickSlot) {
                    module.retryTimer.reset();
                    return;
                }

                if (module.retryTimer.passed(500L)) {
                    module.checkRetry();
                    return;
                }

                if (module.auto.getValue()) {

                    if (module.debug.getValue()) {
                        Logger.getLogger().log(TextColor.RED + "Trying to break block", false);
                    }

                    module.tryBreak(pickSlot);

                    if (module.debug.getValue()) {
                        Logger.getLogger().log(TextColor.RED + "Sent dig packets", false);
                    }
                }

            } else {
                module.canBreak = false;
                module.executed = false;
                module.retryTimer.reset();
                module.pingTimer.reset();
            }
        }
    }

}
