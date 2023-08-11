package me.chachoox.lithium.api.util.movement;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

import java.util.Objects;

public class MovementUtil implements Minecraftable {

    public static double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

    public static double[] directionSpeedNoForward(double speed) {
        float forward = 1f;

        if (mc.gameSettings.keyBindLeft.isPressed()
                || mc.gameSettings.keyBindRight.isPressed()
                || mc.gameSettings.keyBindBack.isPressed()
                || mc.gameSettings.keyBindForward.isPressed()) {
            forward = mc.player.movementInput.moveForward;
        }

        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0) {
            if (side > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (side < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            side = 0;

            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90));
        final double cos = Math.cos(Math.toRadians(yaw + 90));
        final double posX = (forward * speed * cos + side * speed * sin);
        final double posZ = (forward * speed * sin - side * speed * cos);
        return new double[]{posX, posZ};
    }

    public static boolean anyMovementKeys() {
        return mc.player.movementInput.forwardKeyDown
                || mc.player.movementInput.backKeyDown
                || mc.player.movementInput.leftKeyDown
                || mc.player.movementInput.rightKeyDown
                || mc.player.movementInput.jump
                || mc.player.movementInput.sneak;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }

    public static double getDistance2D() {
        double xDist = mc.player.posX - mc.player.prevPosX;
        double zDist = mc.player.posZ - mc.player.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static boolean isMoving() {
        return mc.player.moveForward != 0.0 || mc.player.moveStrafing != 0.0;
    }

    public static void strafe(MoveEvent event, double speed) {
        if (isMoving()) {
            double[] strafe = strafe(speed);
            event.setX(strafe[0]);
            event.setZ(strafe[1]);
        } else {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    public static double[] strafe(double speed) {
        return strafe(mc.player, speed);
    }

    public static double[] strafe(Entity entity, double speed) {
        return strafe(entity, mc.player.movementInput, speed);
    }

    public static double[] strafe(Entity entity, MovementInput movementInput, double speed) {
        float moveForward = movementInput.moveForward;
        float moveStrafe  = movementInput.moveStrafe;
        float rotationYaw = entity.prevRotationYaw
                + (entity.rotationYaw - entity.prevRotationYaw)
                * mc.getRenderPartialTicks();

        if (moveForward != 0.0f) {

            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);

            } else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;

            if (moveForward > 0.0f) {
                moveForward = 1.0f;

            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }

        double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));

        return new double[] {posX, posZ};
    }

    public static double getSpeed() {
        return getSpeed(false);
    }


    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();

            defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }

        if (slowness && mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();

            defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
        }

        return defaultSpeed;
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;

        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            //noinspection ConstantConditions
            int amplifier = mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (amplifier + 1) * 0.1;
        }

        return defaultSpeed;
    }

    public static void setMotion(double x, double y, double z) { //this only works while riding for how is coded, is that intended?
        if (mc.player != null && mc.player.getRidingEntity() != null) {
            if (mc.player.isRiding()) {
                mc.player.getRidingEntity().motionX = x;
                mc.player.getRidingEntity().motionY = y;
                mc.player.getRidingEntity().motionZ = x;
            } else {
                mc.player.motionX = x;
                mc.player.motionY = y;
                mc.player.motionZ = z;
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static double calcEffects(double speed) {
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        if (mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier();
            speed /= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }
}


