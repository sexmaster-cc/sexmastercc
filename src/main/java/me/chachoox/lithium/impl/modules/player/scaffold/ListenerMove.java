package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMove extends ModuleListener<Scaffold, MoveEvent> {
    public ListenerMove(Scaffold module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (mc.player.onGround) {
            if (module.down.getValue() && mc.gameSettings.keyBindSneak.isKeyDown()) {
                event.setSneaking(false);
                return;
            }
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            double increment;
            for (increment = 0.05D; x != 0.0D && isOffsetBBEmpty(x, -1.0D, 0.0D); ) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
            }
            while (z != 0.0D && isOffsetBBEmpty(0.0D, -1.0D, z)) {
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
            while (x != 0.0D && z != 0.0D && isOffsetBBEmpty(x, -1.0D, z)) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
            event.setX(x);
            event.setY(y);
            event.setZ(z);
        }
    }



    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ)).isEmpty();
    }
}