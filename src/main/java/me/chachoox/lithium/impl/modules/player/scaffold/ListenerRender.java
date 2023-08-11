package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender extends ModuleListener<Scaffold, Render3DEvent> {
    public ListenerRender(Scaffold module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (int i = 9; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                if (module.isValid(((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) {
                    if (BlocksManager.get().debugScaffold() && module.pos != null) {
                        AxisAlignedBB bb = new AxisAlignedBB(
                                module.pos.getX() - mc.getRenderManager().viewerPosX,
                                (module.pos.getY() - mc.getRenderManager().viewerPosY),
                                module.pos.getZ() - mc.getRenderManager().viewerPosZ,
                                module.pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                                (module.pos.getY() + 1 - mc.getRenderManager().viewerPosY),
                                module.pos.getZ() + 1 - mc.getRenderManager().viewerPosZ
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
