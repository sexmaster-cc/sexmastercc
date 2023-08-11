package me.chachoox.lithium.impl.modules.render.blockhighlight;

import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.FastBreak;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ListenerRender extends ModuleListener<BlockHighlight, Render3DEvent> {
    public ListenerRender(BlockHighlight module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            Entity player = mc.getRenderViewEntity();

            if (player != null && !mc.objectMouseOver.getBlockPos().equals(Managers.MODULE.get(FastBreak.class).getPos())) {
                IBlockState state = mc.world.getBlockState(pos);
                AxisAlignedBB bb = Interpolation.interpolateAxis(state.getSelectedBoundingBox(mc.world, pos).grow(0.0020000000949949026));
                RenderUtil.startRender();
                switch (module.renderMode.getValue()) {
                    case OUTLINE:
                        RenderUtil.drawOutline(bb, module.lineWidth.getValue(), module.getOutlineColor());
                        break;
                    case BOX:
                        RenderUtil.drawBox(bb, module.getBoxColor());
                        break;
                    case BOTH:
                        RenderUtil.drawBox(bb, module.getBoxColor());
                        RenderUtil.drawOutline(bb, module.lineWidth.getValue(), module.getOutlineColor());
                }
                RenderUtil.endRender();
            }
        }
    }
}
