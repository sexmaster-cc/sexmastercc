package me.chachoox.lithium.impl.modules.combat.holefill;

import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.holefill.util.Priority;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class ListenerMotion extends ModuleListener<HoleFill, MotionUpdateEvent> {
    public ListenerMotion(HoleFill module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (module.isNull()) {
            module.disable();
            return;
        }

        module.holes = module.calcHoles();

        if (module.holes.isEmpty() && module.autoDisable.getValue()) {
            module.disable();
            return;
        }

        final BlockPos playerPos = PositionUtil.getPosition();
        if (!HoleUtil.isHole(playerPos) && !EntityUtil.isOnBurrow(mc.player) && !module.isInDoubleHole(mc.player)) {
            if (module.noSelfFill.getValue()) {
                final Vec3d vec = mc.player.getPositionVector().add(mc.player.motionX, mc.player.motionY, mc.player.motionZ);
                module.holes.removeIf(pos -> pos.distanceSq(vec.x, vec.y, vec.z) < MathUtil.square(module.noSelfFillRange.getValue()) && !checkSelf());
            }

            boolean closest = module.priority.getValue() == Priority.CLOSEST;
            module.holes.sort(Comparator.comparingDouble(pos -> closest ? +BlockUtil.getDistanceSq(pos) : -BlockUtil.getDistanceSq(pos)));
        }

        final Vec3d eyePos = PositionUtil.getEyesPos(mc.player); //idk if this is a good idea
        module.holes.removeIf(pos -> MathUtil.getYDifferenceSq(pos.getY(), eyePos.y) > MathUtil.square(module.vertical.getValue()));

        module.target = EntityUtil.getClosestEnemy();
        if (module.target != null) {
            module.holes.removeIf(p -> BlockUtil.getDistanceSq(module.target, p) > MathUtil.square(module.smartRange.getValue()));
            module.holes.sort(Comparator.comparingDouble(p -> BlockUtil.getDistanceSq(module.target, p)));
        }

        if (module.smart.getValue()) {
            module.target = EntityUtil.getClosestEnemy();
            if (module.target == null
                    || module.target.getDistanceSq(mc.player) > MathUtil.square(module.enemyRange.getValue())
                    || EntityUtil.isPlayerSafe(module.target)
                    || module.isInDoubleHole(module.target)
                    || module.isTrapped(module.target)
                    || module.antiFriend.getValue() && module.calcFriend() != null) {
                return;
            }
        }

        module.onPreEvent(module.holes, event);
    }


    private boolean checkSelf() {
        return EntityUtil.isOnBurrow(mc.player) || module.isDoubleHole(mc.player.getPosition()) || HoleUtil.isHole(PositionUtil.getPosition());
    }
}