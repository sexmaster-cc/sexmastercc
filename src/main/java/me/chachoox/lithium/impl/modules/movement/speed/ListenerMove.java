package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.asm.ducks.IEntity;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;

public class ListenerMove extends ModuleListener<Speed, MoveEvent> {
    public ListenerMove(Speed module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (!MovementUtil.isMoving() && mc.player.isElytraFlying() || mc.player.isOnLadder() || mc.player.isSneaking() && !mc.player.noClip) {
            return;
        }

        if (!module.inLiquids.getValue() && PositionUtil.inLiquid()) {
            return;
        }

        if (module.autoSprint.getValue() && module.canSprint()) {
            mc.player.setSprinting(true);
        }

        if (module.useTimer.getValue()) {
            Managers.TIMER.set(1.0888f);
        }

        float defaultSpeed = 0.2873F;
        switch (module.mode.getValue()) {
            case STRAFE: {
                if (module.strafeStage == 1 && MovementUtil.isMoving()) {
                    module.speed = 1.35 * MovementUtil.calcEffects(defaultSpeed) - 0.01;
                } else if (module.strafeStage == 2 && MovementUtil.isMoving()) {
                    if (!PositionUtil.inLiquid() && !((IEntity) mc.player).getIsInWeb()) {
                        double yMotion = module.jump.getValue().getHeight() + MovementUtil.getJumpSpeed();
                        mc.player.motionY = yMotion;
                        event.setY(yMotion);
                    }
                    module.speed = Managers.KNOCKBACK.shouldBoost(module.kbBoost.getValue()) ? (module.boostReduction.getValue()) / 10.0 : module.speed * (module.boost ? 1.6835 : 1.395);
                } else if (module.strafeStage == 3) {
                    module.speed = Managers.KNOCKBACK.shouldBoost(module.kbBoost.getValue()) ? (module.boostReduction.getValue()) / 10.0 : module.distance - 0.66 * (module.distance - MovementUtil.calcEffects(0.2873));
                    module.boost = !module.boost;
                } else {
                    if ((mc.world.getCollisionBoxes(null, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) && module.strafeStage > 0) {
                        module.strafeStage = MovementUtil.isMoving() ? 1 : 0;
                    }
                    module.speed = module.distance - module.distance / 159.0;
                }

                module.speed = Math.min(module.speed, MovementUtil.calcEffects(10.0));
                module.speed = Math.max(module.speed, MovementUtil.calcEffects(defaultSpeed));
                MovementUtil.strafe(event, module.speed);
                if (MovementUtil.isMoving()) {
                    module.strafeStage++;
                }
                break;
            }
            case STRAFESTRICT: {
                if (module.strafeStage == 1 && MovementUtil.isMoving()) {
                    module.speed = 1.35 * MovementUtil.calcEffects(defaultSpeed) - 0.01;
                } else if (module.strafeStage == 2 && MovementUtil.isMoving()) {
                    if (!PositionUtil.inLiquid() && !((IEntity) mc.player).getIsInWeb()) {
                        double yMotion = module.jump.getValue().getHeight() + MovementUtil.getJumpSpeed();
                        mc.player.motionY = yMotion;
                        event.setY(yMotion);
                    }
                    module.speed = Managers.KNOCKBACK.shouldBoost(module.kbBoost.getValue()) ? (module.boostReduction.getValue()) : module.speed * (module.boost ? 1.6835 : 1.395);
                } else if (module.strafeStage == 3) {
                    module.speed = Managers.KNOCKBACK.shouldBoost(module.kbBoost.getValue()) ? (module.boostReduction.getValue()) : module.distance - 0.66 * (module.distance - MovementUtil.calcEffects(0.2873));
                    module.boost = !module.boost;
                } else {
                    if ((mc.world.getCollisionBoxes(null, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically) && module.strafeStage > 0) {
                        module.strafeStage = MovementUtil.isMoving() ? 1 : 0;
                    }
                    module.speed = module.distance - module.distance / 159.0;
                }

                double speed = 0.465;
                double restrictedSpeed = 0.44;

                module.speed = Math.min(module.speed, MovementUtil.calcEffects(10.0));
                module.speed = Math.max(module.speed, MovementUtil.calcEffects(defaultSpeed));
                module.speed = Math.min(module.speed, module.strictTicks > 25 ? speed : restrictedSpeed);
                MovementUtil.strafe(event, module.speed);
                module.strictTicks++;
                if (module.strictTicks > 50) {
                    module.strictTicks = 0;
                }
                if (MovementUtil.isMoving()) {
                    module.strafeStage++;
                }
                break;
            }
            case ONGROUND: {
                if (mc.player.onGround || module.onGroundStage == 3) {
                    if ((!mc.player.collidedHorizontally && mc.player.moveForward != 0.0f) || mc.player.moveStrafing != 0.0f) {
                        if (module.onGroundStage == 2) {
                            module.speed *= 2.149;
                            module.onGroundStage = 3;
                        } else if (module.onGroundStage == 3) {
                            module.onGroundStage = 2;
                            module.speed = module.distance - 0.66 * (module.distance - MovementUtil.getSpeed());
                        } else if (PositionUtil.isBoxColliding() || mc.player.collidedVertically) {
                            module.onGroundStage = 1;
                        }
                    }
                    module.speed = Math.min(module.speed, MovementUtil.calcEffects(10.0));
                    module.speed = Math.max(module.speed, MovementUtil.calcEffects(defaultSpeed));
                    MovementUtil.strafe(event, module.speed);
                }
                break;
            }
        }
    }
}
