package me.chachoox.lithium.api.util.movement;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

import static me.chachoox.lithium.api.util.entity.EntityUtil.requirePositionEntity;

// this is annoying in rotation util
public class PositionUtil implements Minecraftable {

    public static Vec3d getEyesPos(final Entity entity) {
        return new Vec3d(entity.posX, getEyeHeight(entity), entity.posZ);
    }

    public static double getEyeHeight(final Entity entity) {
        return entity.posY + entity.getEyeHeight();
    }

    public static BlockPos getPosition() {
        return getPosition(mc.player);
    }

    public static BlockPos getPosition(Entity entity) {
        return getPosition(entity, 0.0);
    }

    public static BlockPos getPosition(Entity entity, double yOffset) {
        double y = entity.posY + yOffset;
        if (entity.posY - Math.floor(entity.posY) > 0.5) {
            y = Math.ceil(entity.posY);
        }

        return new BlockPos(entity.posX, y, entity.posZ);
    }

    public static Entity getPositionEntity() {
        EntityPlayerSP player = mc.player;
        Entity ridingEntity;
        return player == null ? null : (ridingEntity = player.getRidingEntity()) != null && !(ridingEntity instanceof EntityBoat) ? ridingEntity : player;
    }

    public static Set<BlockPos> getBlockedPositions(Entity entity) {
        return getBlockedPositions(entity.getEntityBoundingBox());
    }

    public static Set<BlockPos> getBlockedPositions(AxisAlignedBB bb) {
        return getBlockedPositions(bb, 0.5);
    }

    public static Set<BlockPos> getBlockedPositions(AxisAlignedBB bb, double offset) {
        Set<BlockPos> positions = new HashSet<>();

        double y = bb.minY;
        if (bb.minY - Math.floor(bb.minY) > offset) {
            y = Math.ceil(bb.minY);
        }

        positions.add(new BlockPos(bb.maxX, y, bb.maxZ));
        positions.add(new BlockPos(bb.minX, y, bb.minZ));
        positions.add(new BlockPos(bb.maxX, y, bb.minZ));
        positions.add(new BlockPos(bb.minX, y, bb.maxZ));

        return positions;
    }

    public static boolean inLiquid() {
        return inLiquid(MathHelper.floor(requirePositionEntity().getEntityBoundingBox().minY + 0.01));
    }

    public static boolean inLiquid(boolean feet) {
        return inLiquid(MathHelper.floor(mc.player.getEntityBoundingBox().minY - (feet ? 0.03 : 0.2)));
    }

    private static boolean inLiquid(int y) {
        return getState(y) != null;
    }
    public static boolean isMovementBlocked() {
        IBlockState state = findState(Block.class, MathHelper.floor(mc.player.getEntityBoundingBox().minY - 0.01));
        return state != null && state.getMaterial().blocksMovement();
    }


    private static IBlockState getState(int y) {
        Entity entity = requirePositionEntity();
        int startX = MathHelper.floor(entity.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor(entity.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil(entity.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil(entity.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                IBlockState s = mc.world.getBlockState(new BlockPos(x, y, z));
                if (s.getBlock() instanceof BlockLiquid) {
                    return s;
                }
            }
        }
        return null;
    }
    public static boolean isBoxColliding() {
        return mc.world.getCollisionBoxes(mc.player,
                        mc.player
                                .getEntityBoundingBox()
                                .offset(0.0, 0.21, 0.0))
                .size() > 0;
    }

    @SuppressWarnings("SameParameterValue")
    private static IBlockState findState(Class<? extends Block> block, int y) {
        int startX = MathHelper.floor(mc.player.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor(mc.player.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil(mc.player.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil(mc.player.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                IBlockState s = mc.world.getBlockState(new BlockPos(x, y, z));
                if (block.isInstance(s.getBlock())) {
                    return s;
                }
            }
        }

        return null;
    }
}
