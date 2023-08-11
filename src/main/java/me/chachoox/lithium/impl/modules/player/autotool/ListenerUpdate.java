package me.chachoox.lithium.impl.modules.player.autotool;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerUpdate extends ModuleListener<AutoTool, UpdateEvent> {
    public ListenerUpdate(AutoTool module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.set && !mc.gameSettings.keyBindAttack.isKeyDown() && module.swapBackTimer.passed(module.swapBackTicks.getValue() * 50)) {
            ItemUtil.switchTo(module.lastSlot);
            module.reset();
        }
    }
}
