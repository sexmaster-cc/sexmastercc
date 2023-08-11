package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.misc.EatEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerEat extends ModuleListener<Announcer, EatEvent> {
    public ListenerEat(Announcer module) {
        super(module, EatEvent.class);
    }

    @Override
    public void call(EatEvent event) {
        if (event.getEntity() == mc.player && module.eat.getValue()) {
            module.foodStack = event.getStack();
            module.addEvent(Type.EAT);
        }
    }
}
