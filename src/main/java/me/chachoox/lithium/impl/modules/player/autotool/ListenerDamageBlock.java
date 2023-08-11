package me.chachoox.lithium.impl.modules.player.autotool;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.blocks.DamageBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;

public class ListenerDamageBlock extends ModuleListener<AutoTool, DamageBlockEvent> {
    public ListenerDamageBlock(AutoTool module) {
        super(module, DamageBlockEvent.class);}

    @Override
    public void call(DamageBlockEvent event) {
        if (MineUtil.canBreak(event.getPos()) && !mc.player.isCreative() && mc.gameSettings.keyBindAttack.isKeyDown()) {
            int slot = MineUtil.findBestTool(event.getPos());
            if (slot != -1) {
                if (!module.set) {
                    module.lastSlot = mc.player.inventory.currentItem;
                    module.set = true;
                }
                if (module.swapTimer.passed(module.swapTicks.getValue() * 50)) {
                    FastBreak FAST_BREAK = Managers.MODULE.get(FastBreak.class);
                    if (FAST_BREAK.isEnabled() && !FAST_BREAK.isBlockValid(mc.world.getBlockState(event.getPos()).getBlock())) {
                        ItemUtil.switchTo(slot);
                    } else if (!FAST_BREAK.isEnabled()) {
                        ItemUtil.switchTo(slot);
                    }
                }
            }
        }
        else if (module.set) {
            if (module.swapTimer.passed(module.swapBackTicks.getValue() * 50)) {
                ItemUtil.switchTo(module.lastSlot);
                module.reset();
            }
        }
    }
}
