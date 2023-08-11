package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;

public class ListenerMotion extends ModuleListener<Jesus, MotionUpdateEvent> {
    public ListenerMotion(Jesus module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (mc.player.isDead || mc.player.isSneaking() && module.timer.passed(600)) {
            return;
        }

        if (event.getStage() == Stage.PRE && module.mode.getValue() == JesusMode.TRAMPOLINE) {
            if (PositionUtil.inLiquid(false) && !mc.player.isSneaking()) {
                mc.player.onGround = false;
            }

            Block block = mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock();

            if (module.jumped && !mc.player.capabilities.isFlying && !mc.player.isInWater()) {
                if (mc.player.motionY < -0.3 || mc.player.onGround || mc.player.isOnLadder()) {
                    module.jumped = false;
                    return;
                }

                mc.player.motionY = mc.player.motionY / 0.9800000190734863 + 0.08;
                mc.player.motionY -= 0.03120000000005;
            }

            if (mc.player.isInWater() || mc.player.isInLava()) {
                mc.player.motionY = 0.1;
            }

            if (!mc.player.isInLava() && block instanceof BlockLiquid && mc.player.motionY < 0.2) {
                mc.player.motionY = 0.5;
                module.jumped = true;
            }
        }

        if (event.getStage() == Stage.PRE && !PositionUtil.inLiquid() && PositionUtil.inLiquid(true) && !PositionUtil.isMovementBlocked() && mc.player.ticksExisted % 2 == 0) {
            event.setY(event.getY() + 0.02);
        }
    }
}
