package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.entity.AttackUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class ListenerMotion extends ModuleListener<FastBreak, MotionUpdateEvent> {
    public ListenerMotion(FastBreak module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            if (module.rotation.getValue() == RotationsEnum.NORMAL && module.pos != null && !module.rotationTimer.passed(150L)) {
                float[] rotations = RotationUtil.getRotations(module.pos);
                RotationUtil.doRotation(RotationsEnum.NORMAL, rotations);
            }

            Entity entity = null;
            double distance;
            if (module.pos != null && module.pos.equals(module.crystalPos)) {
                for (Entity crystal : mc.world.loadedEntityList) {
                    if (crystal instanceof EntityEnderCrystal
                            && !crystal.isDead
                            && (distance = mc.player.getDistanceSq(crystal)) < MathUtil.square(module.range.getValue())
                            && (mc.player.canEntityBeSeen(crystal) || distance < MathUtil.square(3.3F))) {
                        entity = crystal;
                    }
                }

                if (entity != null && entity.getEntityId() == module.crystalID) {
                    BlockPos newCrystalPos = new BlockPos(entity.posX, entity.posY, entity.posZ).down();
                    if (module.crystalPos.equals(newCrystalPos) && module.crystalTimer.passed(getCrystalDelay()) && module.getBlock() == Blocks.AIR) {
                        if (module.crystalRetries >= 8) {
                            module.crystalID = -1;
                            module.crystalRetries = 0;
                            return;
                        }

                        float[] angle = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
                        if (module.rotation.getValue() == RotationsEnum.NORMAL) {
                            Managers.ROTATION.setRotations(angle[0], angle[1]);
                        } else if (module.rotation.getValue() == RotationsEnum.PACKET) {
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], angle[1], mc.player.onGround));
                        }

                        AttackUtil.attackEntity(entity, module.rotation.getValue(), SwingEnum.PACKET, true);
                        module.crystalRetries++;
                        module.crystalTimer.reset();
                    }
                }
            }
        }
    }

    private Integer getCrystalDelay() {
        if (mc.player != null) {
            if (NetworkUtil.getLatencyNoSpoof() < 25) {
                return 50;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 50) {
                return 25;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 100) {
                return 15;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 200) {
                return 0;
            }
        }
        return 25;
    }
}
