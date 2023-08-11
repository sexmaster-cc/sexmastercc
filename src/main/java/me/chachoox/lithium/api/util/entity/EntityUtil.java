package me.chachoox.lithium.api.util.entity;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.asm.ducks.IEntityLiving;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.potion.Potion;
import net.minecraft.world.Explosion;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityUtil implements Minecraftable {

    private static final List<Block> burrowList = Arrays.asList(
            Blocks.BEDROCK,
            Blocks.OBSIDIAN,
            Blocks.ENDER_CHEST,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.BEACON,
            Blocks.PISTON,
            Blocks.REDSTONE_BLOCK,
            Blocks.ENCHANTING_TABLE,
            Blocks.ANVIL
    );

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closest = null;
        double distance = Float.MAX_VALUE;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != null && !isDead(player) && !player.equals(mc.player) && !Managers.FRIEND.isFriend(player)) {
                Vec3d pos = mc.player.getPositionVector();
                double dist = player.getDistanceSq(pos.x, pos.y, pos.z);
                if (dist < distance) {
                    closest = player;
                    distance = dist;
                }
            }
        }

        return closest;
    }

    public static boolean isPlayerSafe(EntityPlayer player) {
        final Vec3d playerVec = player.getPositionVector();
        final BlockPos position = new BlockPos(playerVec);
        return HoleUtil.isHole(position) || isOnBurrow(player);
    }

    @SuppressWarnings("DataFlowIssue")
    public static double getDefaultMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isntValid(EntityPlayer entity, double range) {
        return mc.player.getDistance(entity) > range || entity == mc.player || isDead(entity) || Managers.FRIEND.isFriend(entity);
    }

    public static float getHealth(EntityPlayer player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    @SuppressWarnings("ConstantConditions")
    public static float calculate(double posX, double posY, double posZ, EntityLivingBase entity, boolean breakBlocks) {
        double v = (1.0D - entity.getDistance(posX, posY, posZ) / 12.0D) * getBlockDensity(breakBlocks, new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        return getBlastReduction(entity, getDamageMultiplied((float) ((v * v + v) / 2.0 * 85.0 + 1.0)), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
        damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        final int k = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), ds);
        damage = damage * (1.0F - MathHelper.clamp(k, 0.0F, 20.0F) / 25.0F);

        if (entity.isPotionActive(MobEffects.RESISTANCE)) {
            damage = damage - (damage / 4);
        }

        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        final int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static boolean isDead(Entity entity) {
        return getHealth(entity) <= 0 || entity.isDead;
    }

    public static float getHealth(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return ((EntityPlayer) entity).getHealth() + ((EntityPlayer) entity).getAbsorptionAmount();
        }
        return 0;
    }

    public static double getBlockDensity(boolean blockDestruction, Vec3d vector, AxisAlignedBB bb) {
        double diffX = 1 / ((bb.maxX - bb.minX) * 2D + 1D);
        double diffY = 1 / ((bb.maxY - bb.minY) * 2D + 1D);
        double diffZ = 1 / ((bb.maxZ - bb.minZ) * 2D + 1D);
        double diffHorizontal = (1 - Math.floor(1D / diffX) * diffX) / 2D;
        double diffTranslational = (1 - Math.floor(1D / diffZ) * diffZ) / 2D;
        if (diffX >= 0 && diffY >= 0 && diffZ >= 0) {
            float solid = 0;
            float nonSolid = 0;

            for (double x = 0; x <= 1; x = x + diffX) {
                for (double y = 0; y <= 1; y = y + diffY) {
                    for (double z = 0; z <= 1; z = z + diffZ) {
                        double scaledDiffX = bb.minX + (bb.maxX - bb.minX) * x;
                        double scaledDiffY = bb.minY + (bb.maxY - bb.minY) * y;
                        double scaledDiffZ = bb.minZ + (bb.maxZ - bb.minZ) * z;
                        if (!isSolid(new Vec3d(scaledDiffX + diffHorizontal, scaledDiffY, scaledDiffZ + diffTranslational), vector, blockDestruction)) {
                            solid++;
                        }
                        nonSolid++;
                    }
                }
            }

            return solid / nonSolid;
        } else {
            return 0;
        }
    }

    public static boolean isNaked(EntityPlayer player) {
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack == null || stack.isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean isSolid(Vec3d start, Vec3d end, boolean blockDestruction) {
        int currX = MathHelper.floor(start.x);
        int currY = MathHelper.floor(start.y);
        int currZ = MathHelper.floor(start.z);
        int endX = MathHelper.floor(end.x);
        int endY = MathHelper.floor(end.y);
        int endZ = MathHelper.floor(end.z);
        BlockPos blockPos = new BlockPos(currX, currY, currZ);
        IBlockState blockState = mc.world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if ((blockState.getCollisionBoundingBox(mc.world, blockPos) != Block.NULL_AABB)
                && block.canCollideCheck(blockState, false)
                && ((BlockUtil.resistantBlocks.contains(block)
                || BlockUtil.unbreakableBlocks.contains(block))
                | !blockDestruction)) {
            return true;
        }

        double seDeltaX = end.x - start.x;
        double seDeltaY = end.y - start.y;
        double seDeltaZ = end.z - start.z;
        int steps = 200;
        while (steps-- >= 0) {
            boolean unboundedX = true;
            boolean unboundedY = true;
            boolean unboundedZ = true;
            double stepX = 999;
            double stepY = 999;
            double stepZ = 999;
            double deltaX = 999;
            double deltaY = 999;
            double deltaZ = 999;
            if (endX > currX) {
                stepX = currX + 1;
            } else if (endX < currX) {
                stepX = currX;
            } else {
                unboundedX = false;
            }

            if (endY > currY) {
                stepY = currY + 1.0;
            } else if (endY < currY) {
                stepY = currY;
            } else {
                unboundedY = false;
            }

            if (endZ > currZ) {
                stepZ = currZ + 1.0;
            } else if (endZ < currZ) {
                stepZ = currZ;
            } else {
                unboundedZ = false;
            }

            if (unboundedX) {
                deltaX = (stepX - start.x) / seDeltaX;
            }

            if (unboundedY) {
                deltaY = (stepY - start.y) / seDeltaY;
            }

            if (unboundedZ) {
                deltaZ = (stepZ - start.z) / seDeltaZ;
            }

            // -1.0E-4 ???
            if (deltaX == 0) {
                deltaX = -1.0E-4;
            }

            if (deltaY == 0) {
                deltaY = -1.0E-4;
            }

            if (deltaZ == 0) {
                deltaZ = -1.0E-4;
            }
            EnumFacing facing;

            if (deltaX < deltaY && deltaX < deltaZ) {
                facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
            } else if (deltaY < deltaZ) {
                facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
            } else {
                facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
            }
            currX = MathHelper.floor(start.x) - (facing == EnumFacing.EAST ? 1 : 0);
            currY = MathHelper.floor(start.y) - (facing == EnumFacing.UP ? 1 : 0);
            currZ = MathHelper.floor(start.z) - (facing == EnumFacing.SOUTH ? 1 : 0);
            blockPos = new BlockPos(currX, currY, currZ);
            blockState = mc.world.getBlockState(blockPos);
            block = blockState.getBlock();
            if (block.canCollideCheck(blockState, false) && ((BlockUtil.resistantBlocks.contains(block) || BlockUtil.unbreakableBlocks.contains(block)) || !blockDestruction)) {
                return true;
            }
        }
        return false;
    }

    public static Entity requirePositionEntity() {
        return Objects.requireNonNull(PositionUtil.getPositionEntity());
    }

    public static boolean inLiquid(boolean feet) {
        return inLiquid(MathHelper.floor(requirePositionEntity().getEntityBoundingBox().minY - (feet ? 0.03 : 0.2)));
    }

    private static boolean inLiquid(int y) {
        return findState(BlockLiquid.class, y) != null;
    }

    @SuppressWarnings("SameParameterValue")
    private static IBlockState findState(Class<? extends Block> block, int y) {
        Entity entity = requirePositionEntity();
        int startX = MathHelper.floor(entity.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor(entity.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil(entity.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil(entity.getEntityBoundingBox().maxZ);
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

    public static EntityPlayer getClosestEnemy(BlockPos pos, List<EntityPlayer> list) {
        return getClosestEnemy(pos.getX(), pos.getY(), pos.getZ(), list);
    }

    public static EntityPlayer getClosestEnemy(double x, double y, double z, List<EntityPlayer> players) {
        EntityPlayer closest = null;
        double distance = 3.4028234663852886E38;// What
        for (EntityPlayer player : players) {
            double dist;
            if (player == null || isDead(player) || player.equals(mc.player) || Managers.FRIEND.isFriend(player) || !((dist = player.getDistanceSq(x, y, z)) < distance))
                continue;
            closest = player;
            distance = dist;
        }
        return closest;
    }

    public static boolean isOnBurrow(EntityPlayer player) {
        BlockPos pos = PositionUtil.getPosition(player);
        return isBurrow(pos, player) || isBurrow(pos.up(), player);
    }

    public static boolean isBurrow(BlockPos pos, EntityPlayer player) {
        IBlockState state = mc.world.getBlockState(pos);
        return burrowList.contains(state.getBlock()) && state.getBoundingBox(mc.world, pos).offset(pos).maxY > player.posY;
    }

    public static boolean isPassiveMob(Entity entity) {
        if (entity instanceof EntityWolf) {
            return !((EntityWolf) entity).isAngry();
        }
        if (entity instanceof EntityIronGolem) {
            return ((EntityIronGolem) entity).getRevengeTarget() == null;
        }
        return entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid;
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity) || entity instanceof EntitySpider;
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie && !((EntityPigZombie) entity).isAngry() || entity instanceof EntityWolf
                && !((EntityWolf) entity).isAngry() || entity instanceof EntityEnderman && ((EntityEnderman) entity).isScreaming();
    }

    public static void swingClient() {
        if (!mc.player.isSwingInProgress || mc.player.swingProgressInt >= ((IEntityLiving) mc.player).getArmSwingAnim() / 2 || mc.player.swingProgressInt < 0) {
            mc.player.swingProgressInt = -1;
            mc.player.isSwingInProgress = true;
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
    }
}

