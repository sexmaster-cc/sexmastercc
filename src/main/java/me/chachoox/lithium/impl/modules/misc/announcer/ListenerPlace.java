package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.blocks.PlaceBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

//this only works with "vanilla" block placing
public class ListenerPlace extends ModuleListener<Announcer, PlaceBlockEvent> {
    public ListenerPlace(Announcer module) {
        super(module, PlaceBlockEvent.class);
    }

    @Override
    public void call(PlaceBlockEvent event) {
        if (module.place.getValue()) {
            module.placeStack = event.getStack();
            module.addEvent(Type.PLACE);
        }
    }
}
