package me.chachoox.lithium.api.util.blocks;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleUtil implements Minecraftable {

    public static boolean isObbyHole(BlockPos pos) {
        return !isBedrockHole(pos)
                && (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK)
                && (mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK)
                && (mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK)
                && (mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK)
                && (mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK)
                && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR
                && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
    }

    public static boolean isBedrockHole(BlockPos pos) {
        return mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                && mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR
                && mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
    }

    public static BlockPos isDoubleObby(BlockPos pos) {
        if (
                (mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR
                        && (mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos.east()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.east().up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.east().up(2)).getMaterial() == Material.AIR
        ) {
            return isDoubleBedrock(pos) == null ? new BlockPos(1, 0, 0) : null;
        } else if (
                (mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR
                        && (mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos.south()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.south().up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.south().up(2)).getMaterial() == Material.AIR
        ) {
            return isDoubleBedrock(pos) == null ? new BlockPos(0, 0, 1) : null;
        }
        return null;
    }

    public static BlockPos isDoubleBedrock(BlockPos pos) {
        if (
                (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR
                        && (mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos.east()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.east().up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.east().up(2)).getMaterial() == Material.AIR
        ) {
            return new BlockPos(1, 0, 0);
        } else if (
                (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.up(2)).getMaterial() == Material.AIR
                        && (mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.BEDROCK)
                        && (mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.BEDROCK)
                        && mc.world.getBlockState(pos.south()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.south().up()).getMaterial() == Material.AIR
                        && mc.world.getBlockState(pos.south().up(2)).getMaterial() == Material.AIR
        ) {
            return new BlockPos(0, 0, 1);
        }
        return null;
    }

    public static boolean isHole(BlockPos pos) {
        return isObbyHole(pos) || isBedrockHole(pos);
    }

    public static boolean isDoubleHole(BlockPos pos) {
        final BlockPos validTwoBlockObby = isDoubleObby(pos);
        if (validTwoBlockObby != null) {
            return true;
        }

        final BlockPos validTwoBlockBedrock = isDoubleBedrock(pos);
        return validTwoBlockBedrock != null;
    }

    public static TwoBlockPos getDouble(BlockPos pos) {
        BlockPos twoPos = HoleUtil.isDoubleBedrock(pos);
        if (twoPos == null) {
            twoPos = HoleUtil.isDoubleObby(pos);
        }

        TwoBlockPos twoBlockPos = null;
        if (twoPos != null) {
            twoBlockPos = new TwoBlockPos(pos, pos.add(twoPos.getX(), twoPos.getY(), twoPos.getZ()));
        }

        return twoBlockPos;
    }
}
