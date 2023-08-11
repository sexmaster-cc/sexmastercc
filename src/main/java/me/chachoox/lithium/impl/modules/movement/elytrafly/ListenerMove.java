package me.chachoox.lithium.impl.modules.movement.elytrafly;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;

public class ListenerMove extends ModuleListener<ElytraFly, MoveEvent> {
    public ListenerMove(ElytraFly module) {
        super(module, MoveEvent.class, 5000);
    }

    @Override
    public void call(MoveEvent event) {
        if (module.stopInWater.getValue() && mc.player.isInsideOfMaterial(Material.WATER)) {
            return;
        }

        if (!mc.player.onGround && !mc.player.isElytraFlying()
                && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA
                && mc.gameSettings.keyBindJump.isKeyDown() && module.autoStart.getValue()) {
            if (module.startTimer.passed(350)) {
                Managers.TIMER.set(0.17f);
                PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                module.startTimer.reset();
            }
        } else {
            Managers.TIMER.set(1.0f);
        }

        if (module.isElytra()) {

            //ctrl
            if (!module.wasp.getValue() && !module.boost.getValue()) {
                if (!mc.player.movementInput.forwardKeyDown
                        && !mc.player.movementInput.sneak) {
                    mc.player.setVelocity(0.0, 0.0, 0.0);
                } else if (mc.player.movementInput.forwardKeyDown
                        && (module.vertical.getValue()
                        || mc.player.prevRotationPitch > 0.0F)) {
                    float yaw = (float) Math.toRadians(mc.player.rotationYaw);
                    double speed = module.hSpeed.getValue() / 5;
                    mc.player.motionX = MathHelper.sin(yaw) * -speed;
                    mc.player.motionZ = MathHelper.cos(yaw) * speed;
                    return;
                }
            }

            //wasp
            if (module.wasp.getValue()) {
                double vSpeed = mc.gameSettings.keyBindJump.isKeyDown() ? module.vSpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -module.vSpeed.getValue() : 0;

                event.setY(vSpeed);
                mc.player.setVelocity(0, 0, 0);
                mc.player.motionY = vSpeed;
                mc.player.moveVertical = (float) vSpeed;

                if (!MovementUtil.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setX(0);
                    event.setY(0);
                    event.setY(0);
                    return;
                }
                MovementUtil.strafe(event, module.hSpeed.getValue() / 5);
            }

            //boost
            if (module.boost.getValue()) {
                if (mc.player.movementInput.jump) {
                    float yaw = mc.player.rotationYaw * 0.017453292f;
                    mc.player.motionX -= MathHelper.sin(yaw) * 0.15f;
                    mc.player.motionZ += MathHelper.cos(yaw) * 0.15f;
                }
            }
        }
    }
}
