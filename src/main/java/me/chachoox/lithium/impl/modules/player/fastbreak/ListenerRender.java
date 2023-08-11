package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;

public class ListenerRender extends ModuleListener<FastBreak, Render3DEvent> {
    public ListenerRender(FastBreak module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (!mc.player.capabilities.isCreativeMode && module.getPos() != null) {
            IBlockState state = mc.world.getBlockState(module.getPos());
            AxisAlignedBB bb = Interpolation.interpolateAxis(state.getSelectedBoundingBox(mc.world, module.getPos()).grow(0.0020000000949949026));

            if (module.mode.getValue() != MineMode.INSTANT && module.getBlock() == Blocks.AIR) {
                return;
            }

            if (module.isBlockValid(mc.world.getBlockState(module.getPos()).getBlock())) {
                RenderUtil.startRender();
                RenderUtil.drawBox(bb, getColorByDamage());
                RenderUtil.drawOutline(bb, module.lineWidth.getValue(), getWireColorByDamage());
                RenderUtil.endRender();
            }
        }
    }

    private Color getColorByDamage() {
        return module.maxDamage >= 0.9f ? ColorUtil.changeAlpha(Color.GREEN, module.alpha.getValue()) : ColorUtil.changeAlpha(Color.RED, module.alpha.getValue());
    }

    private Color getWireColorByDamage() {
        return module.maxDamage >= 0.9f ? ColorUtil.changeAlpha(Color.GREEN, module.lineAlpha.getValue()) : ColorUtil.changeAlpha(Color.RED, module.lineAlpha.getValue());
    }
}
