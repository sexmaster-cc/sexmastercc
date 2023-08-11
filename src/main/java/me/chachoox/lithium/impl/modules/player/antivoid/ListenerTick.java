package me.chachoox.lithium.impl.modules.player.antivoid;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerTick extends ModuleListener<AntiVoid, TickEvent> {
    public ListenerTick(AntiVoid module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            return;
        }
        switch (module.mode.getValue()) {
            case STOP: {
                if (check()) {
                    if (mc.player.posY < 0) {
                        mc.player.motionY = 0;
                    }
                }
                break;
            }
            case ANTI: {
                if (check()) {
                    if (mc.player.posY < -10) {
                        return;
                    }
                    if (mc.player.posY < -5) {
                        mc.player.motionY = 1;
                    }
                }
                break;
            }
        }
    }

    private boolean check() {
        return !mc.player.isSpectator() || mc.player.noClip;
    }
}
