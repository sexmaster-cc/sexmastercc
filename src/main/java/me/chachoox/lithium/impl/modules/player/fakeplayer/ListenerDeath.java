package me.chachoox.lithium.impl.modules.player.fakeplayer;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerDeath extends ModuleListener<FakePlayer, DeathEvent> {
    public ListenerDeath(FakePlayer module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (event.getEntity() == mc.player) {
            module.disable();
        }
    }
}
