package me.chachoox.lithium.impl.modules.combat.autotrap;

import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.autotrap.util.Trap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.*;
import java.util.stream.Collectors;

public class AutoTrap extends BlockPlaceModule {

    private static final EnumFacing[] TOP_FACINGS = new EnumFacing[]
            {
                    EnumFacing.UP,
                    EnumFacing.NORTH,
                    EnumFacing.WEST,
                    EnumFacing.SOUTH,
                    EnumFacing.EAST
            };

    protected final NumberProperty<Float> targetRange =
            new NumberProperty<>(
                    7.0f, 1.0f, 15.0f, 0.1f,
                    new String[]{"TargetRange", "TargetDistance"},
                    "How close we have to be to someone for them to be a target."
            );

    protected final NumberProperty<Float> range =
            new NumberProperty<>(
                    4.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"PlaceRange", "Range", "PlaceDistance", "Distance"},
                    "How close we have to be to the target to trap them."
            );

    protected final NumberProperty<Integer> extend =
            new NumberProperty<>(
                    1, 1, 3,
                    new String[]{"Extend", "Extension", "wrap"},
                    "How much we want to extend the trap if the player is in a 2x1 or something that isnt a 1x1."
            );

    protected final Property<Boolean> logoutSpots =
            new Property<>(
                    false,
                    new String[]{"LogoutSpots", "Logs", "logSpots"},
                    "Traps logout spots if there are no other players in range."
            );

    protected final Property<Boolean> noStep =
            new Property<>(
                    false,
                    new String[]{"AntiStep", "NoStep"},
                    "Places an extra block on top of the targets head to make it harder to step out."
            );

    protected final Property<Boolean> noScaffold =
            new Property<>(
                    false,
                    new String[]{"AntiScaffold", "NoScaffold"},
                    "Places a block on top of the targets head to make it harder to scaffold out."
            );

    protected final Property<Boolean> upperBody =
            new Property<>(
                    false,
                    new String[]{"Upperbody", "Chest"},
                    "Places blocks around the targets upper body."
            );

    protected final Property<Boolean> upperFace =
            new Property<>(
                    false,
                    new String[]{"UpperFace", "FaceBlock"},
                    "Places blocks around the targets face."
            );

    protected final Property<Boolean> jumpDisable =
            new Property<>(
                    false,
                    new String[]{"JumpDisable", "AutoDisable"},
                    "Disables autotrap if we are higher than when the module was enabled."
            );

    protected final Map<EntityPlayer, List<BlockPos>> cached = new HashMap<>();
    protected final Map<EntityPlayer, Double> speeds = new HashMap<>();

    protected List<BlockPos> placeList;
    public EntityPlayer target;

    public AutoTrap() {
        super("AutoTrap", new String[]{"Autotrap", "trap", "encase"}, "Traps enemies.", Category.COMBAT);
        this.offerProperties(targetRange, range, extend, noStep, noScaffold, upperBody, upperFace, jumpDisable);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
    }

    @Override
    public void clear() {
        cached.clear();
        speeds.clear();
        target = null;
    }

    @Override
    public String getSuffix() {
        return target != null ? target.getName() : null;
    }

    protected void getTargets() {
        cached.clear();
        updateSpeed();
        EntityPlayer newTarget = calcTarget();

        target = newTarget;
        if (newTarget == null) {
            return;
        }

        List<BlockPos> newTrapping = cached.get(newTarget);
        if (newTrapping == null) {
            newTrapping = getPositions(newTarget);
        }

        placeList = newTrapping;
    }

    private List<BlockPos> getPositions(EntityPlayer player) {
        List<BlockPos> blocked = new ArrayList<>();
        BlockPos playerPos = new BlockPos(player);
        if (CombatUtil.isHole(playerPos, false)[0] || extend.getValue() == 1) {
            blocked.add(playerPos.up());
        } else {
            List<BlockPos> unfiltered = new ArrayList<>(PositionUtil.getBlockedPositions(player)).stream().sorted(Comparator.comparingDouble(BlockUtil::getDistanceSq)).collect(Collectors.toList());
            List<BlockPos> filtered = new ArrayList<>(unfiltered).stream().filter(pos -> mc.world.getBlockState(pos).getMaterial().isReplaceable() && mc.world.getBlockState(pos.up()).getMaterial().isReplaceable()).collect(Collectors.toList());

            if (extend.getValue() == 3 && filtered.size() == 2 && unfiltered.size() == 4) {
                if (unfiltered.get(0).equals(filtered.get(0)) && unfiltered.get(3).equals(filtered.get(1))) {
                    filtered.clear();
                }
            }

            if (extend.getValue() == 2 && filtered.size() > 2 || extend.getValue() == 3 && filtered.size() == 3) {
                while (filtered.size() > 2) {
                    filtered.remove(filtered.size() - 1);
                }
            }

            for (BlockPos pos : filtered) {
                blocked.add(pos.up());
            }
        }

        if (blocked.isEmpty()) {
            blocked.add(playerPos.up());
        }

        List<BlockPos> positions = positionsFromBlocked(blocked);
        positions.sort(Comparator.comparingDouble(pos -> -BlockUtil.getDistanceSq(pos)));
        positions.sort(Comparator.comparingInt(Vec3i::getY));

        return positions.stream().filter(pos -> BlockUtil.getDistanceSq(pos) <= MathUtil.square(range.getValue())).collect(Collectors.toList());
    }

    private List<BlockPos> positionsFromBlocked(List<BlockPos> blockedIn) {
        List<BlockPos> positions = new ArrayList<>();
        if (!noStep.getValue() && !blockedIn.isEmpty()) {
            BlockPos[] helping = findTopHelping(blockedIn, true);
            for (int i = 0; i < helping.length; i++) {
                BlockPos pos = helping[i];
                if (pos != null) {
                    if (i == 1 && !upperBody.getValue() && (!blockedIn.contains(PositionUtil.getPosition().up()) || !upperFace.getValue()) && helping[5] != null) {
                        positions.add(helping[5]);
                    }

                    positions.add(helping[i]);
                    break;
                }
            }
        }

        blockedIn.forEach(pos -> positions.addAll(applyOffsets(pos, Trap.TOP, positions)));

        if (upperBody.getValue() || upperFace.getValue() && blockedIn.contains(PositionUtil.getPosition().up())) {
            blockedIn.forEach(pos -> positions.addAll(applyOffsets(pos, Trap.OFFSETS, positions)));
        }

        if (blockedIn.size() == 1) {
            if (noScaffold.getValue()) {
                blockedIn.forEach(pos -> positions.addAll(applyOffsets(pos, Trap.NO_SCAFFOLD, positions)));
            }

            if (noStep.getValue()) {
                blockedIn.forEach(pos -> positions.addAll(applyOffsets(pos, Trap.NO_STEP, positions)));
            }
        }

        return positions;
    }

    private List<BlockPos> applyOffsets(BlockPos pos, Vec3i[] offsets, List<BlockPos> alreadyAdded) {
        ArrayList<BlockPos> positions = new ArrayList<>();
        for (Vec3i vec3i : offsets) {
            BlockPos offset = pos.add(vec3i);
            if (alreadyAdded.contains(offset)) continue;
            positions.add(offset);
        }
        return positions;
    }

    private BlockPos[] findTopHelping(List<BlockPos> positions, boolean first) {
        BlockPos[] bestPos = new BlockPos[] {null, null, null, null, positions.get(0).up().north(), null};
        for (BlockPos pos : positions) {
            BlockPos up = pos.up();
            for (EnumFacing facing : TOP_FACINGS) {
                BlockPos helping = up.offset(facing);
                if (!mc.world.getBlockState(helping).getMaterial().isReplaceable()) {
                    bestPos[0] = helping;
                    return bestPos;
                }

                EnumFacing helpingFace = BlockUtil.getFacing(helping);
                byte blockingFactor = helpingEntityCheck(helping);
                if (helpingFace == null) {
                    switch (blockingFactor) {
                        case 0:
                            if (first && bestPos[5] == null) {
                                List<BlockPos> hPositions = new ArrayList<>();
                                for (BlockPos hPos : positions) {
                                    hPositions.add(hPos.down());
                                }

                                bestPos[5] = findTopHelping(hPositions, false)[0];
                            } else {
                                break;
                            }

                            bestPos[1] = helping;
                            break;
                        case 1:
                            bestPos[3] = helping;
                            break;
                        case 2:
                            break;
                    }
                } else {
                    switch (blockingFactor) {
                        case 0:
                            bestPos[0] = helping;
                            break;
                        case 1:
                            bestPos[2] = helping;
                            break;
                        case 2:
                            break;
                    }
                }
            }
        }

        return bestPos;
    }

    private byte helpingEntityCheck(BlockPos pos) {
        byte blocking = 0;
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity == null || EntityUtil.isDead(entity) || !entity.preventEntitySpawning || (entity instanceof EntityPlayer && !entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos)))) {
                continue;
            }

            return 2;
        }

        return blocking;
    }

    protected EntityPlayer calcTarget() {
        EntityPlayer closest = null;
        double distance = Double.MAX_VALUE;
        for (EntityPlayer player : mc.world.playerEntities) {
            double playerDist = mc.player.getDistanceSq(player);
            if (playerDist < distance && isValid(player)) {
                closest = player;
            }
        }

        return closest;
    }

    private boolean isValid(EntityPlayer player) {
        if (player != null && !EntityUtil.isDead(player) && !player.equals(mc.player) && !Managers.FRIEND.isFriend(player) && player.getDistanceSq(mc.player) <= MathUtil.square(targetRange.getValue())) {
            if (getSpeed(player) <= 22.0F) {
                List<BlockPos> positions = getPositions(player);
                cached.put(player, positions);
                return positions.stream().anyMatch(pos -> mc.world.getBlockState(pos).getMaterial().isReplaceable());
            }
            return true;
        }
        return false;
    }

    protected void updateSpeed() {
        for (EntityPlayer player : mc.world.playerEntities) {
            double xDist = player.posX - player.prevPosX;
            double yDist = player.posY - player.prevPosY;
            double zDist = player.posZ - player.prevPosZ;
            double speed = xDist * xDist + yDist * yDist + zDist * zDist;

            speeds.put(player, speed);
        }
    }

    private double getSpeed(EntityPlayer player) {
        Double playerSpeed = speeds.get(player);
        if (playerSpeed != null) {
            return Math.sqrt(playerSpeed) * 20 * 3.6;
        }

        return 0.0;
    }

    public boolean trapLogs() {
        return isEnabled() && logoutSpots.getValue();
    }
}
