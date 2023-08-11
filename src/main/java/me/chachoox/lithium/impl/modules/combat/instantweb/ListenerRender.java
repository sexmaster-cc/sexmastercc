package me.chachoox.lithium.impl.modules.combat.instantweb;

import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class ListenerRender extends ModuleListener<InstantWeb, Render3DEvent> {
    public ListenerRender(InstantWeb module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (BlocksManager.get().debugWeb() && module.target != null) {
            for (BlockPos pos : module.getPlacements()) {
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockAir) {
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
