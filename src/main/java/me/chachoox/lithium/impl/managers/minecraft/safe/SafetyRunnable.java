package me.chachoox.lithium.impl.managers.minecraft.safe;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.Sphere;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.thread.SafeRunnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class SafetyRunnable implements Minecraftable, SafeRunnable {
    private final SafeManager manager;
    private final List<Entity> crystals;

    public SafetyRunnable(SafeManager manager, List<Entity> crystals) {
        this.manager = manager;
        this.crystals = crystals;
    }

    @Override
    public void runSafely() {
        final float maxDamage = 4.0F;
        for (Entity entity : crystals) {
            if (entity instanceof EntityEnderCrystal && !entity.isDead) {
                float damage = DamageUtil.calculate(entity, mc.player);
                if (damage > maxDamage || damage > EntityUtil.getHealth(mc.player) - 1.0) {
                    manager.setSafe(false);
                    return;
                }
            }
        }

        boolean fullArmor = true;
        for (ItemStack stack : mc.player.inventory.armorInventory) {
            if (stack.isEmpty()) {
                fullArmor = false;
                break;
            }
        }

        Vec3d playerVec = mc.player.getPositionVector();
        BlockPos position = new BlockPos(playerVec);
        if (fullArmor && position.getY() == playerVec.y) {
            if (HoleUtil.isHole(position)) {
                manager.setSafe(true);
                return;
            }
        }

        if (manager.isSafe()) {
            return;
        }

        BlockPos playerPos = PositionUtil.getPosition();
        int x = playerPos.getX();
        int y = playerPos.getY();
        int z = playerPos.getZ();
        int maxRadius = Sphere.getRadius(6.0);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 1; i < maxRadius; i++) {
            Vec3i v = Sphere.get(i);
            pos.setPos(x + v.getX(), y + v.getY(), z + v.getZ());
            if (BlockUtil.canPlaceCrystal(pos, false)) {
                float damage = DamageUtil.calculate(pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, mc.player, getBB());
                if (damage > maxDamage || damage > EntityUtil.getHealth(mc.player) - 1.0) {
                    manager.setSafe(false);
                    return;
                }
            }
        }

        manager.setSafe(true);
    }

    public AxisAlignedBB getBB() {
        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        float w = mc.player.width / 2.0f;
        float h = mc.player.height;
        return new AxisAlignedBB(x - w, y, z - w, x + w, y + h, z + w);
    }
}
