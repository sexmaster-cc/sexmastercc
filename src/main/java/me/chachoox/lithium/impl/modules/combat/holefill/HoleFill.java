package me.chachoox.lithium.impl.modules.combat.holefill;

import me.chachoox.lithium.api.module.BlockPlaceModule;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.holefill.util.Priority;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HoleFill extends BlockPlaceModule {

    protected final NumberProperty<Float> horizontal =
            new NumberProperty<>(
                    4.5f, 1f, 6f, 0.1f,
                    new String[]{"HorizontalDist", "HorizontalDistance", "holeRangeXZ"},
                    "Horizontal distance for the hole range."
            );

    protected final NumberProperty<Float> vertical =
            new NumberProperty<>(
                    4.0f, 1f, 6f, 0.1f,
                    new String[]{"VerticalDist", "VerticalDistance", "holerangeY"},
                    "Vertical distance for the hole range."
            );

    protected final EnumProperty<Priority> priority =
            new EnumProperty<>(Priority.FARTHEST,
                    new String[]{"Priority", "prio"},
                    "What holes should it fill first."
            );

    protected final Property<Boolean> smart =
            new Property<>(
                    false,
                    new String[]{"Smart", "Genius", "300Iq", "alberteinstein"},
                    "Avoids filling holes when a player isnt in the range but the module is enabled."
            );

    protected final Property<Boolean> noSelfFill =
            new Property<>(
                    false,
                    new String[]{"NoSelfFill", "NoHoleFillSelf", "AntiSelf"},
                    "Avoids filling holes that are close to us which might holefill ourself."
            );

    protected final NumberProperty<Float> noSelfFillRange =
            new NumberProperty<>(
                    2.5f, 1f, 3.0f, 0.1f,
                    new String[]{"NoSelfFillRange", "NoSelfFillDistance"},
                    "How close a hole can be to us before it is considered holefilling ourself."
            );

    protected final NumberProperty<Float> smartRange =
            new NumberProperty<>(
                    3f, 1f, 6f, 0.1f,
                    new String[]{"SmartRange", "GeniusRange", "300IqRange"},
                    "How close an enemy has to be to us before allowing us to holefill."
            );

    protected final NumberProperty<Float> enemyRange =
            new NumberProperty<>(
                    8f, 1f, 10f, 0.1f,
                    new String[]{"EnemyRange", "TargetRange"},
                    "How close a player has to be to us before being selected as an enemy."
            );

    protected final Property<Boolean> twoByOne =
            new Property<>(
                    true,
                    new String[]{"2x1", "Long", "Doubles", "longHoles"},
                    "Fills holes that are 2 blocks long and 1 block wide."
            );

    protected final Property<Boolean> antiFriend =
            new Property<>(true,
                    new String[]{"FriendProtect", "antifriend"},
                    "Wont place if a friend is nearby."
            );

    protected final Property<Boolean> autoDisable =
            new Property<>(
                    true,
                    new String[]{"AutoDisable", "Disable"},
                    "Disables holefill when there are no holes to be filled."
            );

    protected List<BlockPos> holes = new ArrayList<>();
    protected EntityPlayer target;

    public HoleFill() {
        super("HoleFill", new String[]{"HoleFill", "antihole", "holefiller", "noholes"}, "Attempts to fill holes near your opponent.", Category.COMBAT);
        this.offerProperties(horizontal, vertical, priority, smart, antiFriend, noSelfFill, noSelfFillRange, smartRange, enemyRange, twoByOne, autoDisable);
        this.offerListeners(new ListenerMotion(this), new ListenerRender(this));
        this.smart.addObserver(event -> holes.clear());
    }

    @Override
    public void clear() {
        holes.clear();
        target = null;
    }

    protected List<BlockPos> calcHoles() {
        ArrayList<BlockPos> holes = new ArrayList<>();
        List<BlockPos> positions = BlockUtil.getSphere(horizontal.getValue(), false);
        for (BlockPos pos : positions) {
            if (isHole(pos)) {
                holes.add(pos);
            }
        }
        return holes;
    }

    private boolean isHole(BlockPos pos) {
        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityArrow)) {
                return false;
            }
        }

        if (twoByOne.getValue()) {
            BlockPos twoPos = HoleUtil.isDoubleBedrock(pos);
            if (twoPos == null) {
                twoPos = HoleUtil.isDoubleObby(pos);
            }
            if (twoPos != null) {
                TwoBlockPos twoBlockPos = new TwoBlockPos(pos, pos.add(twoPos.getX(), twoPos.getY(), twoPos.getZ()));
                //dont move this or it breaks
                //noinspection RedundantIfStatement
                if (target != null && (twoBlockPos.getOne().equals(PositionUtil.getPosition(target)) || twoBlockPos.getTwo().equals(PositionUtil.getPosition(target)))) {
                    return false;
                }
                return true;
            }
        }
        return HoleUtil.isHole(pos);
    }

    protected boolean isInDoubleHole(EntityPlayer player) {
        return isDoubleHole(player.getPosition());
    }

    protected boolean isDoubleHole(BlockPos pos) {
        final BlockPos validTwoBlockObby = HoleUtil.isDoubleObby(pos);
        if (validTwoBlockObby != null) {
            return true;
        }

        final BlockPos validTwoBlockBedrock = HoleUtil.isDoubleBedrock(pos);
        return validTwoBlockBedrock != null;
    }

    protected EntityPlayer calcFriend() {
        EntityPlayer friend = null;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != mc.player && Managers.FRIEND.isFriend(player) && (target != null && target.getDistanceSq(player) < MathUtil.square(2.5F))) {
                friend = player;
            }
        }

        return friend;
    }

    protected boolean isTrapped(EntityPlayer player) {
        Vec3d vec = player.getPositionVector();
        BlockPos pos = new BlockPos(vec);
        return !mc.world.getBlockState(pos.up(2)).getMaterial().isReplaceable();
    }
}
