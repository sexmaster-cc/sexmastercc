package me.chachoox.lithium.impl.managers.minecraft;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;

public class RotationManager extends SubscriberImpl implements Minecraftable {

    private float yaw, pitch;
    private boolean rotated;
    private int ticksSinceNoRotate;
    private double x, y, z;
    private boolean onGround;

    public RotationManager() {
        this.listeners.add(new Listener<MotionUpdateEvent>(MotionUpdateEvent.class, Integer.MAX_VALUE) {
            @Override
            public void call(MotionUpdateEvent event) {
                switch (event.getStage()) {
                    case PRE: {
                        x = mc.player.posX;
                        y = mc.player.posY;
                        z = mc.player.posZ;
                        onGround = mc.player.onGround;
                        yaw = mc.player.rotationYaw;
                        pitch = mc.player.rotationPitch;
                        break;
                    }
                    case POST: {
                        mc.player.posX = x;
                        mc.player.posY = y;
                        mc.player.posZ = z;
                        mc.player.onGround = onGround;
                        ticksSinceNoRotate++;
                        if (ticksSinceNoRotate > 2) {
                            rotated = false;
                        }
                        mc.player.rotationYaw = yaw;
                        mc.player.rotationYawHead = yaw;
                        mc.player.rotationPitch = pitch;
                        break;
                    }
                }
            }
        });
    }

    public void setRotations(float yaw, float pitch) {
        rotated = true;
        ticksSinceNoRotate = 0;
        mc.player.rotationYaw = yaw;
        mc.player.rotationYawHead = yaw;
        mc.player.rotationPitch = pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public boolean isRotated() {
        return rotated;
    }
}
