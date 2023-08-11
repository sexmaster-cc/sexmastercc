package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.Swap;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.AxisAlignedBB;

public class ListenerRender extends ModuleListener<AutoCrystal, Render3DEvent> {
    public ListenerRender(AutoCrystal module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (module.multiTask.getValue()) {
            if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItemUseAction().equals(EnumAction.EAT) || mc.player.getActiveItemStack().getItemUseAction().equals(EnumAction.DRINK)) {
                return;
            }
        }

        if (module.whileMining.getValue()) {
            if (ItemUtil.isHolding(ItemTool.class) && mc.playerController.getIsHittingBlock() && MineUtil.canBreak(mc.objectMouseOver.getBlockPos()) && !mc.world.isAirBlock(mc.objectMouseOver.getBlockPos())) {
                return;
            }
        }

        int crystalSlot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);

        if (crystalSlot == -1 && !module.offhand) {
            return;
        }

        if ((module.swap.getValue() == Swap.NONE || module.swap.getValue() == Swap.NORMAL) && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !module.offhand) {
            return;
        }

        if (module.renderPos != null) {
            RenderUtil.startRender();
            AxisAlignedBB bb = Interpolation.interpolatePos(module.renderPos, 1.0F);
            RenderUtil.drawBox(bb, Colours.get().getColourCustomAlpha(30));
            RenderUtil.drawOutline(bb, 1.6F, Colours.get().getColour());
            RenderUtil.endRender();
        }
    }
}
