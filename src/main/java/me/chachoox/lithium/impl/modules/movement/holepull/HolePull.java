package me.chachoox.lithium.impl.modules.movement.holepull;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;
import me.chachoox.lithium.impl.modules.movement.step.Step;
import me.chachoox.lithium.impl.modules.render.holeesp.util.TwoBlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HolePull extends Module {

    protected final EnumProperty<PullMode> mode =
            new EnumProperty<>(
                    PullMode.PULL,
                    new String[]{"Mode", "Type", "method"},
                    "Pull: - Old HolePull, basically anchor " +
                            "/ Snap: - Holesnap, like kami5/sn0w/cascade"
            );

    public NumberProperty<Integer> pitch =
            new NumberProperty<>(
                    60, 0, 90,
                    new String[]{"Pitch", "BITCH", "pitc"},
                    "How low we have to be looking to get pulled towards a hole. (Requires mode Pull)"
            );

    public NumberProperty<Float> range =
            new NumberProperty<>(
                    4.0f, 1.0f, 6.0f, 0.5f,
                    new String[]{"Range", "distance", "dist"},
                    "Maximum distance to hole. (Requires mode Snap)"
            );

    protected final Property<Boolean> step =
            new Property<>(
                    false,
                    new String[]{"Step", "Up"},
                    "If you want to enable step with this (Requires mode Snap)"
            );

    protected final Property<Boolean> timer =
            new Property<>(
                    true,
                    new String[]{"Timer", "tickshift", "tickspeed"},
                    "Makes you faster (Requires mode Snap)"
            );

    public NumberProperty<Float> timerAmount =
            new NumberProperty<>(
                    4.0f, 1.0f, 5.0f, 0.1f,
                    new String[]{"TimerAmount", "timerspeed"},
                    "Timer speed. (Requires mode Snap)"
            );

    public NumberProperty<Integer> timerLength =
            new NumberProperty<>(
                    25, 10, 100,
                    new String[]{"TimerLength", "tickshiftlength"},
                    "The amount of time for which timer is used. (Requires mode Snap)"
            );

    protected boolean anchoring;
    protected int stuck;
    protected int boosted;
    protected BlockPos hole;
    protected Vec3d twoVec;

    public HolePull() {
        super("HolePull", new String[]{"HolePull", "HoleSnap", "HP", "HoleMove", "HoleWalk"}, "Moves you towards holes.", Category.MOVEMENT);
        this.offerProperties(mode, pitch, range, step, timer, timerAmount, timerLength);
        this.offerListeners(new ListenerMove(this), new ListenerUpdate(this));
        this.mode.addObserver(event -> reset());
    }

    @Override
    public void onEnable() {
        if (mode.getValue() == PullMode.SNAP) {
            if (step.getValue()) {
                Managers.MODULE.get(Step.class).enableNoMessage();
            }
            anchoring = true;
            hole = getTarget(range.getValue());
            if (hole == null) {
                Logger.getLogger().log(TextColor.RED + "<HolePull> Couldn't find a hole.", 45088);
                disable();
            }
        }
    }

    @Override
    public void onDisable() {
        if (step.getValue()) {
            Managers.MODULE.get(Step.class).disableNoMessage();
        }
        Managers.TIMER.reset();
        reset();
    }

    public boolean isAnchoring() {
        return anchoring;
    }

    protected boolean isPitchDown() {
        return mc.player.rotationPitch >= pitch.getValue();
    }

    private void reset() {
        twoVec = null;
        anchoring = false;
        stuck = 0;
        boosted = 0;
    }

    protected void doPull(MoveEvent event, BlockPos pos) {
        Vec3d playerPos = mc.player.getPositionVector();

        Vec3d targetPos;
        if (HoleUtil.isDoubleHole(pos)) {
            TwoBlockPos doubleHole = HoleUtil.getDouble(pos);
            targetPos = getTwo(doubleHole);
            twoVec = targetPos;
        } else {
            targetPos = new Vec3d((double) pos.getX() + 0.5, mc.player.posY, (double) pos.getZ() + 0.5);
        }

        double yawRad = Math.toRadians(RotationUtil.getRotationTo(playerPos, targetPos).x);
        double dist = playerPos.distanceTo(targetPos);
        double speed = mc.player.onGround ? -Math.min(0.2805, dist / 2.0) : -EntityUtil.getDefaultMoveSpeed() + 0.02;

        //Logger.getLogger().log("bro: " + targetPos.x + " no: " + targetPos.y + " way: " + targetPos.z, false);

        if (dist < 0.1) {
            event.setX(0.0);
            event.setZ(0.0);
            return;
        }

        event.setX(-Math.sin(yawRad) * speed);
        event.setZ(Math.cos(yawRad) * speed);
    }

    protected boolean isSafe(Vec3d vec) {
        if (vec == null) {
            return false;
        }

        final Vec3d playerVec = mc.player.getPositionVector();
        double dist = playerVec.distanceTo(vec);
        return dist < 0.1;
    }

    private Vec3d getTwo(TwoBlockPos twoPos) {
        BlockPos one = twoPos.getOne();
        BlockPos two = twoPos.getTwo();

        double x = ((one.getX() + 0.5) + (two.getX() + 0.5)) / 2.0;
        double z = ((one.getZ() + 0.5) + (two.getZ() + 0.5)) / 2.0;

        return new Vec3d(x, mc.player.posY, z);
    }

    private List<BlockPos> getHoles(float range) {
        ArrayList<BlockPos> holes = new ArrayList<>();
        List<BlockPos> circle = BlockUtil.getSphere(range, false);
        for (BlockPos pos : circle) {
            if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR)
                continue;

            if (HoleUtil.isObbyHole(pos) || HoleUtil.isBedrockHole(pos) || HoleUtil.isDoubleHole(pos)) {
                holes.add(pos);
            }
        }

        return holes;
    }

    private BlockPos getTarget(float range) {
        return getHoles(range)
                .stream()
                .filter(hole -> mc.player
                        .getPositionVector()
                        .distanceTo(new Vec3d(
                                (double) hole.getX() + 0.5,
                                mc.player.posY,
                                (double) hole.getZ() + 0.5)) <= range).min(
                        Comparator.comparingDouble(hole -> mc.player.getPositionVector().distanceTo(new Vec3d(
                                (double) hole.getX() + 0.5,
                                mc.player.posY,
                                (double) hole.getZ() + 0.5)))).orElse(null);
    }
}
