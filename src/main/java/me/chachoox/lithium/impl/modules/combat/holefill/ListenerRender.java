package me.chachoox.lithium.impl.modules.combat.holefill;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Collectors;

public class ListenerRender extends ModuleListener<HoleFill, Render3DEvent> {
    public ListenerRender(HoleFill module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (BlocksManager.get().debugHoleFill()) {
            if (!module.holes.isEmpty()) {
                for (BlockPos pos : module.holes) {
                    if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                    {
                        EntityPlayer nearest = EntityUtil.getClosestEnemy(pos,
                                mc.world.getLoadedEntityList()
                                .stream()
                                .filter(entity -> entity instanceof EntityPlayer)
                                .map(entity -> (EntityPlayer) entity)
                                .collect(Collectors.toList()));
                        if (module.smart.getValue()) {
                            if (nearest == null) {
                                return;
                            }
                            if (!(nearest.getDistanceSqToCenter(pos) <= MathUtil.square(module.enemyRange.getValue()))) {
                                return;
                            }
                            if (!(nearest.getDistanceSqToCenter(pos) <= MathUtil.square(module.horizontal.getValue()))) {
                                return;
                            }
                        }
                        AxisAlignedBB bb = new AxisAlignedBB(
                                pos.getX() - mc.getRenderManager().viewerPosX,
                                (pos.getY() - mc.getRenderManager().viewerPosY),
                                pos.getZ() - mc.getRenderManager().viewerPosZ,
                                pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                                (pos.getY() + 1 - mc.getRenderManager().viewerPosY),
                                pos.getZ() + 1 - mc.getRenderManager().viewerPosZ
                        );
                        RenderUtil.startRender();
                        RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(40));
                        RenderUtil.drawOutline(bb, 1.3F, Colours.get().getColour());
                        RenderUtil.endRender();
                    }
                }
            }
        }
    }
}