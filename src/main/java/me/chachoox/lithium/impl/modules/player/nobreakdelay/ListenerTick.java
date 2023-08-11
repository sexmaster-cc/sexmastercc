package me.chachoox.lithium.impl.modules.player.nobreakdelay;

import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerTick extends ModuleListener<NoBreakDelay, TickEvent> {
    public ListenerTick(NoBreakDelay module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (event.isSafe()) {
            IPlayerControllerMP controller = (IPlayerControllerMP) mc.playerController;
            controller.setBlockHitDelay(0);
        }
    }
}
