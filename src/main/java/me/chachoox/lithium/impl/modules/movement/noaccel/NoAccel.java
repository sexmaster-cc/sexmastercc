package me.chachoox.lithium.impl.modules.movement.noaccel;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.managers.minecraft.movement.KnockbackManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;

import java.util.Objects;

public class NoAccel extends Module {

    protected final Property<Boolean> kbBoost =
            new Property<>(
                    false,
                    KnockbackManager.KB_BOOST_ALIAS,
                    KnockbackManager.KB_BOOST_DESCRIPTION
            );

    protected final NumberProperty<Float> boostReduction =
            new NumberProperty<>(
                    4.5f, 1.0f, 5.0f, 0.1f,
                    KnockbackManager.BOOST_REDUCTION_ALIAS,
                    KnockbackManager.BOOST_REDUCTION_DESCRIPTION
            );

    protected final Property<Boolean> inWater =
            new Property<>(
                    true,
                    new String[]{"InWater", "NoAccelInWater", "NoWater"},
                    "Stops cancelling horizontal movement in water."
            );

    protected final Property<Boolean> sneak =
            new Property<>(
                    true,
                    new String[]{"NoSneak", "AntiSneak", "Sneak"},
                    "Stops cancelling horizontal movement when sneaking."
            );

    protected final Property<Boolean> whileEating =
            new Property<>(
                    true,
                    new String[]{"WhileEating", "eating", "noeat"},
                    "Stops if we are eating."
            );

    protected final Property<Boolean> speed =
            new Property<>(
                    true,
                    new String[]{"Speed", "WithSpeed", "UseSpeed"},
                    "Cancels horizontal movement when you have speed."
            );

    protected final Property<Boolean> stopOnLagback =
            new Property<>(
                    false,
                    new String[]{"StopOnLagBack", "stopLag", "noLag"},
                    "Stops cancelling horizontal movement when lagged back."
            );

    protected final Property<Boolean> stopOnTotem =
            new Property<>(
                    false,
                    new String[]{"StopOnTotem", "StopOnPop", "noPop", "noTotem"},
                    "Stops cancelling horizontal movement when you get popped."
            );

    protected final Property<Boolean> slow =
            new Property<>(
                    true,
                    new String[]{"Slowness", "Slowed", "WithSlowness"},
                    "Cancels horizontal movement when you have slowness."
            );

    public NoAccel() {
        super("NoAccel", new String[]{"NoAccel", "InstantSpeed", "FastAccel", "Accel", "Acceleration"}, "Stops horizontal motion to move faster.", Category.MOVEMENT);
        this.offerListeners(new ListenerMove(this));
        this.offerProperties(kbBoost, boostReduction, inWater, sneak, whileEating, stopOnLagback, stopOnTotem, speed, slow);
    }

    protected boolean isEating() {
        if (mc.player.isHandActive()) {
            return (mc.player.getActiveHand() == EnumHand.OFF_HAND && mc.player.getHeldItemOffhand().getItem() instanceof ItemFood)
                    || (mc.player.getActiveHand() == EnumHand.MAIN_HAND && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood);
        }
        return false;
    }

    protected void strafe(MoveEvent event, double speed) {
        if (MovementUtil.isMoving(mc.player)) {
            double[] strafe = strafe(speed);
            event.setX(strafe[0]);
            event.setZ(strafe[1]);
        } else {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    private double[] strafe(double speed) {
        return strafe(mc.player, speed);
    }

    private double[] strafe(Entity entity, double speed) {
        return strafe(entity, mc.player.movementInput, speed);
    }

    private double[] strafe(Entity entity, MovementInput movementInput, double speed) {
        float moveForward = movementInput.moveForward;
        float moveStrafe  = movementInput.moveStrafe;
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.getRenderPartialTicks();

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

    protected double getSpeed(boolean slowness, boolean speed) {
        double defaultSpeed = 0.2873;
        if (speed && mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }

        if (slowness && mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
        }

        return defaultSpeed;
    }
}
