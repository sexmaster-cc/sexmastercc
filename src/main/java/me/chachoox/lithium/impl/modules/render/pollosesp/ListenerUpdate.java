package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.SoundEvents;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerUpdate extends ModuleListener<PollosESP, UpdateEvent> {
    public ListenerUpdate(PollosESP module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.pollosNoise.getValue()) {
            mc.player.playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 1.0f, 1.0f);
        }

        if (module.pollosFps.getValue()) {
            mc.gameSettings.limitFramerate = ThreadLocalRandom.current().nextInt(90);
        }
    }
}
