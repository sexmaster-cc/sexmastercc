package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.impl.event.events.blocks.BlockPushEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerBlockPush extends ModuleListener<Velocity, BlockPushEvent> {
    public ListenerBlockPush(Velocity module) {
        super(module, BlockPushEvent.class);
    }

    @Override
    public void call(BlockPushEvent event) {
        if (module.noPush.getValue()) {
            event.setCanceled(true);
        }
    }
}
