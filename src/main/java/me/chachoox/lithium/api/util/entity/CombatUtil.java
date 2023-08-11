package me.chachoox.lithium.api.util.entity;

import com.google.common.collect.Sets;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

import static net.minecraft.util.EnumFacing.HORIZONTALS;

public class CombatUtil implements Minecraftable {

    public static final Set<Block> NO_BLAST = Sets.newHashSet(
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.ANVIL,
            Blocks.ENDER_CHEST
    );

    public static EntityPlayer getTarget(float range) {
        EntityPlayer currentTarget = null;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range))
                continue;

            if (currentTarget == null) {
                currentTarget = player;
                continue;
            }

            if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(currentTarget)) {
                currentTarget = player;
            }
        }

        return currentTarget;
    }

    public static boolean isInHole(EntityPlayer entity) {
        return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos);
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        final BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (final BlockPos pos : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }

        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        final BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (final BlockPos pos : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static boolean[] isHole(BlockPos pos, boolean above) {
        boolean[] result = new boolean[]{false, true};
        if (!isAir(pos) || !isAir(pos.up()) || above && !isAir(pos.up(2))) {
            return result;
        }

        return is1x1(pos, result);
    }

    public static boolean[] is1x1(BlockPos pos, boolean[] result) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing != EnumFacing.UP) {
                BlockPos offset = pos.offset(facing);
                IBlockState state = mc.world.getBlockState(offset);
                if (state.getBlock() != Blocks.BEDROCK) {
                    if (!NO_BLAST.contains(state.getBlock())) {
                        return result;
                    }

                    result[1] = false;
                }
            }
        }

        result[0] = true;
        return result;
    }

    public static boolean is2x1(BlockPos pos, boolean upper) {
        if (upper && (!isAir(pos) || !isAir(pos.up()) || isAir(pos.down()))) {
            return false;
        }

        int airBlocks = 0;
        for (EnumFacing facing : HORIZONTALS) {
            BlockPos offset = pos.offset(facing);
            if (isAir(offset)) {
                if (isAir(offset.up())) {
                    if (!isAir(offset.down())) {
                        for (EnumFacing offsetFacing: HORIZONTALS) {
                            if (offsetFacing != facing.getOpposite()) {
                                IBlockState state = mc.world.getBlockState(offset.offset(offsetFacing));

                                if (!NO_BLAST.contains(state.getBlock())) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
                airBlocks++;
            }

            if (airBlocks > 1) {
                return false;
            }
        }

        return airBlocks == 1;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        final BlockPos[] touchingBlocks = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
        for (final BlockPos pos : touchingBlocks) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

}
