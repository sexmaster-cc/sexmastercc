package me.chachoox.lithium.impl.modules.combat.aura;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;

public class ListenerMotion extends ModuleListener<Aura, MotionUpdateEvent> {
    public ListenerMotion(Aura module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        module.target = null;
        module.target = module.getTarget();

        if (event.getStage() == Stage.PRE) {
            if (module.target != null) {
                switch (module.sword.getValue()) {
                    case SWITCH: {
                        int swordSlot = ItemUtil.getItemSlot(ItemSword.class);
                        if (swordSlot == -1) {
                            return;
                        }
                        ItemUtil.switchTo(swordSlot);
                        break;
                    }
                    case REQUIRE: {
                        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) break;
                        return;
                    }
                }

                if (module.rotate.getValue()) {
                    float[] rotations = RotationUtil.getRotations(mc.player, module.target, module.bone.getValue().getHeight(), 180);
                    mc.player.rotationYaw = rotations[0];
                    mc.player.rotationPitch = rotations[1];
                }

                float factor = 0;
                switch (module.tpsSync.getValue()) {
                    case AVERAGE: {
                        factor = 20.0F - Managers.TPS.getCurrentTps();
                        break;
                    }
                    case LATEST: {
                        factor = 20.0F - Managers.TPS.getTps();
                        break;
                    }
                }

                boolean stopSneak = module.stopSneak.getValue() && Managers.ACTION.isSneaking();

                boolean stopSprint = module.stopSprint.getValue() && mc.player.isSprinting();

                boolean stopShield = module.stopShield.getValue() && mc.player.isActiveItemStackBlocking();

                if (stopSneak) {
                    PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }

                if (stopSprint) {
                    PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                }

                if (stopShield) {
                    module.releaseShield();
                }

                if (mc.player.getCooledAttackStrength(factor) >= 1.0f) {
                    mc.playerController.attackEntity(mc.player, module.target);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }

                if (stopSneak) {
                    PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }

                if (stopSprint) {
                    PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                }

                if (stopShield) {
                    module.useShield();
                }
            }
        }
    }
}
