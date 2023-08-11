package me.chachoox.lithium.api.util.blocks;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IBlock;
import me.chachoox.lithium.asm.mixins.item.IItemTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

//3arth is a real one bruh
public class MineUtil implements Minecraftable {

    public static boolean canHarvestBlock(BlockPos pos, ItemStack stack) {
        IBlockState state = mc.world.getBlockState(pos);
        state = state.getActualState(mc.world, pos);
        return canHarvestBlock(state, stack);
    }

    public static boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Block block = state.getBlock();
        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }

        if (stack.isEmpty()) {
            return stack.canHarvestBlock(state);
        }

        String tool = ((IBlock) block).getHarvestToolNonForge(state);
        if (tool == null) {
            return stack.canHarvestBlock(state);
        }

        int toolLevel = -1;
        if (stack.getItem() instanceof IItemTool) {
            String toolClass = null;
            if (stack.getItem() instanceof ItemPickaxe) {
                toolClass = "pickaxe";
            }
            else if (stack.getItem() instanceof ItemAxe) {
                toolClass = "axe";
            }
            else if (stack.getItem() instanceof ItemSpade) {
                toolClass = "shovel";
            }

            if (tool.equals(toolClass)) {
                toolLevel = ((IItemTool) stack.getItem()).getToolMaterial().getHarvestLevel();
            }
        }

        if (toolLevel < 0) {
            return stack.canHarvestBlock(state);
        }

        return toolLevel >= ((IBlock) block).getHarvestLevelNonForge(state);
    }

    public static int findBestTool(BlockPos pos) {
        return findBestTool(pos, mc.world.getBlockState(pos));
    }

    public static int findBestTool(BlockPos pos, IBlockState state) {
        int result = mc.player.inventory.currentItem;
        if (state.getBlockHardness(mc.world, pos) > 0) {

            double speed = getSpeed(state, mc.player.getHeldItemMainhand());
            for (int i = 0; i < 9; i++) {

                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                double stackSpeed = getSpeed(state, stack);
                if (stackSpeed > speed) {
                    speed  = stackSpeed;
                    result = i;
                }
            }
        }

        return result;
    }

    public static double getSpeed(IBlockState state, ItemStack stack) {
        double str = stack.getDestroySpeed(state);
        int effect = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
        return Math.max(str + (str > 1.0 ? (effect * effect + 1.0) : 0.0), 0.0);
    }

    public static float getDamage(ItemStack stack, BlockPos pos, boolean onGround) {
        IBlockState state = mc.world.getBlockState(pos);
        return getDamage(state, stack, pos, onGround);
    }

    public static float getDamage(ItemStack stack, BlockPos pos, boolean onGround, boolean isOnGround) {
        IBlockState state = mc.world.getBlockState(pos);
        return getDamage(state, stack, pos, onGround, isOnGround);
    }

    public static float getDamage(IBlockState state, ItemStack stack, boolean onGround) {
        return getDigSpeed(stack, state, onGround, true) / (((IBlock)state.getBlock()).getHardness() * (canHarvestBlock(state, stack) ? 30 : 100));
    }

    public static float getDamage(IBlockState state, ItemStack stack, BlockPos pos, boolean onGround) {
        return getDigSpeed(stack, state, onGround, true) / (state.getBlockHardness(mc.world, pos) * (canHarvestBlock(pos, stack) ? 30 : 100));
    }

    public static float getDamage(IBlockState state, ItemStack stack, BlockPos pos, boolean onGround, boolean isOnGround) {
        return getDigSpeed(stack, state, onGround, isOnGround) / (state.getBlockHardness(mc.world, pos) * (canHarvestBlock(pos, stack) ? 30 : 100));
    }

    private static float getDigSpeed(ItemStack stack, IBlockState state, boolean onGround, boolean isOnGround) {
        float digSpeed = 1.0F;

        if (!stack.isEmpty()) {
            digSpeed *= stack.getDestroySpeed(state);
        }

        if (digSpeed > 1.0F) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);

            if (i > 0 && !stack.isEmpty()) {
                digSpeed += (float)(i * i + 1);
            }
        }

        if (mc.player.isPotionActive(MobEffects.HASTE)) {
            //noinspection ConstantConditions
            digSpeed *= 1.0F + (mc.player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (mc.player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float miningFatigue;
            //noinspection ConstantConditions
            switch (mc.player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0:
                    miningFatigue = 0.3F;
                    break;
                case 1:
                    miningFatigue = 0.09F;
                    break;
                case 2:
                    miningFatigue = 0.0027F;
                    break;
                case 3:
                default:
                    miningFatigue = 8.1E-4F;
            }

            digSpeed *= miningFatigue;
        }

        if (mc.player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(mc.player)) {
            digSpeed /= 5.0F;
        }

        if (onGround && (!isOnGround || !mc.player.onGround)) {
            digSpeed /= 5.0F;
        }

        return (digSpeed < 0 ? 0 : digSpeed);
    }

    public static boolean canBreak(BlockPos pos) {
        return canBreak(mc.world.getBlockState(pos), pos);
    }

    public static boolean canBreak(IBlockState state, BlockPos pos) {
        return state.getBlockHardness(mc.world, pos) != -1 && state.getBlock() != Blocks.AIR && !state.getMaterial().isLiquid();
    }
}
