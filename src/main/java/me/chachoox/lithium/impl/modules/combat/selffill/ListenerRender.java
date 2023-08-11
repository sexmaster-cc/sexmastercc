package me.chachoox.lithium.impl.modules.combat.selffill;

import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender extends ModuleListener<SelfFill, Render3DEvent> {
    public ListenerRender(SelfFill module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (int i = 9; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                if (module.isValid(((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) {
                    if (BlocksManager.get().debugSelfFill()) {
                        if (!(mc.world.getBlockState(PositionUtil.getPosition()).getBlock() instanceof BlockAir)) {
                            return;
                        }
                        AxisAlignedBB bb = new AxisAlignedBB(
                                PositionUtil.getPosition().getX() - mc.getRenderManager().viewerPosX,
                                (PositionUtil.getPosition().getY() - mc.getRenderManager().viewerPosY),
                                PositionUtil.getPosition().getZ() - mc.getRenderManager().viewerPosZ,
                                PositionUtil.getPosition().getX() + 1 - mc.getRenderManager().viewerPosX,
                                (PositionUtil.getPosition().getY() + 1 - mc.getRenderManager().viewerPosY),
                                PositionUtil.getPosition().getZ() + 1 - mc.getRenderManager().viewerPosZ
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
