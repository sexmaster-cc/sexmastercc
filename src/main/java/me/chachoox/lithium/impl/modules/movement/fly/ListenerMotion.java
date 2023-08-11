package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMotion extends ModuleListener<Fly, MotionUpdateEvent> {
    public ListenerMotion(Fly module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        mc.player.motionY = 0.0D;

        if (mc.inGameHasFocus) {

            double speed = module.speed.getValue() / 8;

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = speed;
            }

            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -speed;
            }
        }
    }
}
