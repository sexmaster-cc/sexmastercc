package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.util.EnumHand;

public class ListenerUpdate extends ModuleListener<Animations, UpdateEvent> {
    public ListenerUpdate(Animations module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.swing.getValue() == SwingEnum.NONE) {
            return;
        }

        if (module.swing.getValue() == SwingEnum.CANCEL) {
            mc.player.isSwingInProgress = false;
            return;
        }

        EnumHand hand = null;
        switch (module.swing.getValue()) {
            case OFFHAND: {
                hand = EnumHand.OFF_HAND;
                break;
            }
            case MAINHAND: {
                hand = EnumHand.MAIN_HAND;
                break;
            }
        }

        mc.player.swingingHand = hand;
    }
}
