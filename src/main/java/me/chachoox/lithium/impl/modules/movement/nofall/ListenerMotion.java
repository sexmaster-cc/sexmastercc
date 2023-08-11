package me.chachoox.lithium.impl.modules.movement.nofall;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.nofall.util.NoFallMode;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;


public class ListenerMotion extends ModuleListener<NoFall, MotionUpdateEvent> {
    public ListenerMotion(NoFall module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {

        if (mc.player.onGround && mc.player.fallDistance < 0.5 && mc.player.posY - BlockUtil.getNearestBlockBelow() > 0.625 && mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }

        if (module.mode.getValue() == NoFallMode.BUCKET && module.check()) {
            int slot = ItemUtil.findHotbarItem(Items.WATER_BUCKET);
            if (event.getStage() == Stage.PRE) {
                if (InventoryUtil.isHolding(Items.WATER_BUCKET)) {
                    mc.player.rotationPitch = (90.0f);
                }
            }

            if (!PositionUtil.inLiquid() && module.stopWatch.passed(1000)) {
                ItemUtil.switchTo(slot);
                if (slot == -1) {
                    return;
                }
                mc.playerController.processRightClick(mc.player, mc.world, slot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);

                module.stopWatch.reset();
            }
        }
    }
}
