package me.chachoox.lithium.impl.modules.render.compass;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.compass.util.Direction;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

public class Compass extends Module {

    private final NumberProperty<Float> scale =
            new NumberProperty<>(3.0F, 0.0F, 10.0F, 0.5F,
                    new String[]{"Scale", "s"},
                    "Scale of the compass."
            );

    private final NumberProperty<Integer> posX =
            new NumberProperty<>(0, -500, 500,
                    new String[]{"PosX", "horizontalpos"},
                    "Horizontal position of the compass."
            );

    private final NumberProperty<Integer> posY =
            new NumberProperty<>(0, -500, 500,
                    new String[]{"PosY", "verticalpos"},
                    "Vertical position of the compass."
            );

    public Compass() {
        super("Compass", new String[]{"Compass", "Direction"}, "Draws a compass on your screen.", Category.RENDER);
        this.offerProperties(posX, posY, scale);
        this.offerListeners(new ListenerRender(this));
    }

    protected void onRender(ScaledResolution resolution) {;
        int width = resolution.getScaledWidth() / 2;
        int height = resolution.getScaledHeight() / 2;
        double centerX = width + posX.getValue();
        double centerY = height + posY.getValue();
        for (Direction direction : Direction.values()) {
            double rad = getPosOnCompass(direction);
            Managers.FONT.drawString(direction.name(), (float) (centerX + getX(rad)), (float) (centerY + getY(rad)), direction == Direction.N ? -65536 : -1);
        }
    }

    private double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees(mc.player.rotationYaw));
        int index = dir.ordinal();
        return yaw + (double) index * (Math.PI/2);
    }

    private double getX(double rad) {
        return Math.sin(rad) * (scale.getValue() * 10);
    }

    private double getY(double rad) {// EPIC PITCH
        double pitch = MathHelper.clamp(mc.player.rotationPitch + 30.0f, -90.0f, 90.0f);
        double pitchRadians = Math.toRadians(pitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (scale.getValue() * 10);
    }
}
