package me.chachoox.lithium.api.util.blocks;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtil implements Minecraftable {

    public static final List<Block> resistantBlocks = Arrays.asList(
            Blocks.OBSIDIAN,
            Blocks.ANVIL,
            Blocks.ENCHANTING_TABLE,
            Blocks.ENDER_CHEST,
            Blocks.BEACON
    );

    public static final List<Block> unbreakableBlocks = Arrays.asList(
            Blocks.BEDROCK,
            Blocks.COMMAND_BLOCK,
            Blocks.CHAIN_COMMAND_BLOCK,
            Blocks.END_PORTAL_FRAME,
            Blocks.BARRIER,
            Blocks.PORTAL
    );

    public static double getDistanceSq(BlockPos pos) {
        return getDistanceSq(mc.player, pos);
    }

    public static double getDistanceSq(Entity from, BlockPos to) {
        return from.getDistanceSqToCenter(to);
    }

    public static boolean canPlaceCrystal(final BlockPos blockPos, boolean check) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }


        final BlockPos boost2 = blockPos.add(0, 2, 0);
        if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
            return false;
        }

        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
            if (entity.isDead || entity instanceof EntityEnderCrystal)
                continue;

            return false;
        }

        if (check) {
            for (final Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                if (entity.isDead || entity instanceof EntityEnderCrystal)
                    continue;

                return false;
            }
        }

        return true;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        return getFacing(pos, mc.world);
    }

    public static EnumFacing getFacing(BlockPos pos, IBlockAccess provider) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (!provider.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) {
                return facing;
            }
        }

        return null;
    }

    public static List<BlockPos> getSphere(Entity entity, float radius, boolean ignoreAir) {
        final List<BlockPos> sphere = new ArrayList<>();

        final BlockPos pos = PositionUtil.getPosition(entity);

        final int posX = pos.getX();
        final int posY = pos.getY();
        final int posZ = pos.getZ();

        final int radiuss = (int) radius;

        for (int x = posX - radiuss; x <= posX + radius; x++) {
            for (int z = posZ - radiuss; z <= posZ + radius; z++) {
                for (int y = posY - radiuss; y < posY + radius; y++) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        final BlockPos position = new BlockPos(x, y, z);
                        if (ignoreAir && mc.world.getBlockState(position).getBlock() == Blocks.AIR) {
                            continue;
                        }
                        sphere.add(position);
                    }
                }
            }
        }

        return sphere;
    }

    public static List<BlockPos> getSphere(float radius, boolean ignoreAir) {
        return getSphere(mc.player, radius, ignoreAir);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

    public static boolean isPlayerBlocked(EntityPlayer player) {
        final Vec3d playerVec = player.getPositionVector();
        final BlockPos pos = new BlockPos(playerVec);
        return mc.world.getBlockState(pos.up()).getBlock() != Blocks.AIR;
    }

    public static double getNearestBlockBelow() {
        for (double y = mc.player.posY; y > 0; y -= 0.001) {
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox(mc.world, new BlockPos(0, 0, 0)) != null) {
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock() instanceof BlockSlab) {
                    return -1;
                }
                return y;
            }
        }
        return -1;
    }

    public static boolean isReplaceable(BlockPos pos) {
        return mc.world.getBlockState(pos).getMaterial().isReplaceable();
    }
}