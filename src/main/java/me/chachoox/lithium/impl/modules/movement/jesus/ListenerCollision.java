package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.blocks.CollisionEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class ListenerCollision extends ModuleListener<Jesus, CollisionEvent> {
    public ListenerCollision(Jesus module) {
        super(module, CollisionEvent.class);
    }

    @Override
    public void call(CollisionEvent event) {
        if (event.getEntity() != null && mc.player != null && (event.getEntity().equals(mc.player) || event.getEntity().getControllingPassenger() != null
                && Objects.equals(event.getEntity().getControllingPassenger(), mc.player)) && event.getBlock() instanceof BlockLiquid && !mc.player.isSneaking() && mc.player.fallDistance < 3.0F
                && !PositionUtil.inLiquid() && PositionUtil.inLiquid(false) && isAbove(event.getPos())) {
            BlockPos pos = event.getPos();
            event.setBB(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.99, pos.getZ() + 1));
        }
    }

    private static boolean isAbove(BlockPos pos) {
        return mc.player.getEntityBoundingBox().minY >= pos.getY();
    }
}