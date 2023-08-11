package me.chachoox.lithium.impl.modules.combat.antiregear;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.List;

public class ListenerMotion extends ModuleListener<AntiRegear, MotionUpdateEvent> {
    public ListenerMotion(AntiRegear module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (module.timer.passed(module.delay.getValue()) && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            BlockPos shulkerPos = null;

            EntityPlayer target = EntityUtil.getClosestEnemy();
            if (target == null || mc.player.getDistanceSq(target) > MathUtil.square(module.enemyRange.getValue())) {
                return;
            }

            List<TileEntity> loadedTileEntityList = mc.world.loadedTileEntityList;
            for (TileEntity tileEntity : loadedTileEntityList) {
                if (tileEntity instanceof TileEntityShulkerBox) {
                    BlockPos pos = tileEntity.getPos();
                    if (mc.player.getDistanceSq(pos) < MathUtil.square(module.range.getValue())) {
                        if (module.raytrace.getValue() && !RaytraceUtil.canBlockBeSeen(mc.player, pos, true)) {
                            continue;
                        }
                        shulkerPos = pos;
                        break;
                    }
                }
            }

            if (shulkerPos != null) {
                float[] rotations = RotationUtil.getRotations(shulkerPos);
                Managers.ROTATION.setRotations(rotations[0], rotations[1]);
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1], module.range.getValue());
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, shulkerPos, result.sideHit));
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, shulkerPos, result.sideHit));
                module.timer.reset();
            }
        }
    }
}
