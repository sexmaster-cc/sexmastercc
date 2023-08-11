package me.chachoox.lithium.impl.modules.player.fastplace;

import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerTick extends ModuleListener<FastPlace, TickEvent> {
    public ListenerTick(FastPlace module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            return;
        }

        if (mc.gameSettings.keyBindUseItem.isKeyDown() && module.isValid(mc.player.getHeldItemMainhand().getItem())) {
            if (module.delay.getValue() < ((IMinecraft) mc).getRightClickDelay()) {
                ((IMinecraft) mc).setRightClickDelay(module.delay.getValue());
            }
        }
    }
}
