package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.blocks.BreakBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerBreak extends ModuleListener<Announcer, BreakBlockEvent> {
    public ListenerBreak(Announcer module) {
        super(module, BreakBlockEvent.class);
    }

    @Override
    public void call(BreakBlockEvent event) {
        if (module.mine.getValue()) {
            module.brokenBlock = mc.world.getBlockState(event.getPos()).getBlock();
            module.addEvent(Type.BREAK);
        }
    }
}
