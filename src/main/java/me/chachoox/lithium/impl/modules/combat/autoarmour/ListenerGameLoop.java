package me.chachoox.lithium.impl.modules.combat.autoarmour;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerGameLoop extends ModuleListener<AutoArmour, GameLoopEvent> {
    public ListenerGameLoop(AutoArmour module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        module.runClick();
    }
}
