package me.chachoox.lithium.impl.modules.player.antihitbox;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerUpdate extends ModuleListener<AntiHitBox, UpdateEvent> {
    public ListenerUpdate(AntiHitBox module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.isValid(mc.player.getHeldItemMainhand().getItem())) {
            module.noTrace = true;
            return;
        }

        module.noTrace = false;
    }
}
