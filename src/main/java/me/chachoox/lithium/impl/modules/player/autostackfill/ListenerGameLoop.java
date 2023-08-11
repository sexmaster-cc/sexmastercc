package me.chachoox.lithium.impl.modules.player.autostackfill;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ListenerGameLoop extends ModuleListener<AutoStackFill, GameLoopEvent> {
    public ListenerGameLoop(AutoStackFill module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (mc.player != null && module.timer.passed(module.delay.getValue() * 50L) && !(mc.currentScreen instanceof GuiInventory)) {
            for (int i = 0; i < 9; ++i) {
                module.refillSlot(i);
                module.timer.reset();
            }
        }
    }
}