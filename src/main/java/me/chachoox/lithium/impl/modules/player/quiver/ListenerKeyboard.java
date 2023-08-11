package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.impl.event.events.misc.KeyboardEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerKeyboard extends ModuleListener<Quiver, KeyboardEvent>
{
    public ListenerKeyboard(Quiver module)
    {
        super(module, KeyboardEvent.class);
    }

    @Override
    public void call(KeyboardEvent event) {
        if (module.cycleButton.getValue().getKey() == event.getKey() && event.getEventState()) {
            module.cycle(false, false);
        }
    }

}
