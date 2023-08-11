package me.chachoox.lithium.impl.modules.other.rpc;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerTick extends ModuleListener<RichPresence, TickEvent> {
    public ListenerTick(RichPresence module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (module.imageTimer.passed(30000L)) {
            final String key = module.keys.get(0);
            RichPresence.RPC.presence.largeImageKey = key;
            module.keys.remove(key);
            module.keys.add(key);
            module.imageTimer.reset();
        }
    }
}
