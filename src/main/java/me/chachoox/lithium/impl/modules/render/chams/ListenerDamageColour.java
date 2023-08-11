package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.misc.DamageColorEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

import java.awt.*;

public class ListenerDamageColour extends ModuleListener<Chams, DamageColorEvent> {
    public ListenerDamageColour(Chams module) {
        super(module, DamageColorEvent.class);
    }

    @Override
    public void call(DamageColorEvent event) {
        if (module.damage.getValue()) {
            Color color = module.getDamageColor();
            event.setRed(color.getRed() / 255F);
            event.setGreen(color.getGreen() / 255F);
            event.setBlue(color.getBlue() / 255F);
            event.setAlpha(module.damageColor.getValue().getAlpha() / 255F);
        }
    }
}
