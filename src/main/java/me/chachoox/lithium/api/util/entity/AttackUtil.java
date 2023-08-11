package me.chachoox.lithium.api.util.entity;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.impl.modules.other.blocks.util.SwingEnum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AttackUtil implements Minecraftable {

    public static void attackEntity(Entity entity, RotationsEnum rotation, SwingEnum swing, boolean back) {
        int weaknessSlot = -1;
        if (!DamageUtil.canBreakWeakness(true)) {
            if ((weaknessSlot = DamageUtil.findAntiWeakness()) == -1) {
                return;
            }
        }

        if (entity != null) {
            float[] rotations = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
            RotationUtil.doRotation(rotation, rotations);
            CPacketUseEntity attack = new CPacketUseEntity(entity);
            attack(attack, weaknessSlot, swing, back);
        }
    }

    public static void attack(Packet<?> attacking, int slot, SwingEnum swing, boolean back) {
        int oldSlot = mc.player.inventory.currentItem;
        if (slot != -1) {
            ItemUtil.switchTo(slot);
        }

        PacketUtil.send(attacking);

        switch (swing) {
            case NORMAL: {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case PACKET: {
                PacketUtil.swing();
                break;
            }
        }

        if (slot != -1 && back) {
            ItemUtil.switchTo(oldSlot);
        }
    }

    public static boolean isInterceptedByCrystal(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()))
                continue;
            return true;
        }
        return false;
    }

    public static void attackInPos(BlockPos pos, RotationsEnum rotation, SwingEnum swing, boolean back) {
        if (isInterceptedByCrystal(pos)) {
            EntityEnderCrystal crystal = null;
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity == null || mc.player.getDistance(entity) > 3 || !(entity instanceof EntityEnderCrystal) || entity.isDead)
                    continue;
                crystal = (EntityEnderCrystal) entity;
            }

            if (rotation != null && crystal != null) {
                float[] rotations = RotationUtil.getRotations(crystal.posX, crystal.posY, crystal.posZ);
                RotationUtil.doRotation(rotation, rotations);
            }

            if (crystal != null) {
                attackEntity(crystal, rotation, swing, back);
            }
        }
    }
}