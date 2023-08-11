package me.chachoox.lithium.impl.modules.player.fakerotation;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.item.ItemFood;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerMotion extends ModuleListener<FakeRotation, MotionUpdateEvent> {

    public ListenerMotion(FakeRotation module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (mc.player != null && event.getStage() == Stage.PRE && !canRotate() && !Managers.ROTATION.isRotated()) {

            if (module.randomize.getValue()) {
                mc.player.rotationYaw = ThreadLocalRandom.current().nextInt(-90, 90);
                mc.player.rotationPitch = ThreadLocalRandom.current().nextInt(-180, 180);
            }

            float yaw = ((event.getYaw() + 180) % 360);
            if (!module.randomize.getValue()) {
                if (module.jitter.getValue()) {
                    if (mc.player.ticksExisted % module.speed.getValue() == 0) {
                        module.check = !module.check;
                    }

                    if (module.check) {
                        yaw = yaw + module.limit.getValue();
                    } else {
                        yaw = yaw - module.limit.getValue();
                    }
                }

                mc.player.rotationYaw = yaw;
                mc.player.rotationYawHead = yaw;

                switch (module.pitch.getValue()) {
                    case DOWN: {
                        mc.player.rotationPitch = 90f;
                        break;
                    }
                    case UP: {
                        mc.player.rotationPitch = -90;
                        break;
                    }
                    case ZERO: {
                        mc.player.rotationPitch = 0;
                        break;
                    }
                }
            }
        }
    }

    private boolean canRotate() {
        return ((module.strict.getValue()
                && (!(mc.player.getActiveItemStack().getItem() instanceof ItemFood)
                || mc.gameSettings.keyBindAttack.isKeyDown())
                && (mc.gameSettings.keyBindAttack.isKeyDown()
                || mc.gameSettings.keyBindUseItem.isKeyDown())));
    }
}
