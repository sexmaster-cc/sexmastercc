package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;

//TODO: find a way to not send packets while clicking
public class ListenerClickBlock extends ModuleListener<FastBreak, ClickBlockEvent> {
    public ListenerClickBlock(FastBreak module) {
        super(module, ClickBlockEvent.class);
    }

    @Override
    public void call(ClickBlockEvent event) {
        if (module.pos != null && module.pos.equals(event.getPos())
                && !module.auto.getValue()
                && module.canBreak
                && module.isBlockValid(mc.world.getBlockState(module.getPos()).getBlock())) {
            event.setCanceled(true);
            int pickSlot = MineUtil.findBestTool(module.pos);

            if (module.swap.getValue() == Swap.NONE && mc.player.inventory.currentItem != pickSlot) {
                return;
            }

            module.executed = true;

            if (module.debug.getValue()) {
                Logger.getLogger().log(TextColor.RED + "Trying to break block", false);
            }

            module.tryBreak(pickSlot);

            if (module.debug.getValue()) {
                Logger.getLogger().log(TextColor.RED + "Sent dig packets", false);
            }

            module.resetSwap();
            module.clicks++;
            if (module.clicks >= 3 && module.mode.getValue() == MineMode.PACKET || module.mode.getValue() == MineMode.INSTANT) {
                module.checkRetry();
            }
        }
    }
}