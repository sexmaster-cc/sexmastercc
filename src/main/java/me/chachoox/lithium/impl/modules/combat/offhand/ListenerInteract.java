package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

public class ListenerInteract extends ModuleListener<Offhand, ClickBlockEvent.Right> {
    public ListenerInteract(Offhand module) {
        super(module, ClickBlockEvent.Right.class);
    }

    @Override
    public void call(ClickBlockEvent.Right event) {
        if (event.getHand() == EnumHand.OFF_HAND && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            event.setCanceled(true);
        } else if (event.getHand() == EnumHand.MAIN_HAND) {
            Item mainHand = mc.player.getHeldItemMainhand().getItem();
            Item offHand  = mc.player.getHeldItemOffhand().getItem();
            if (mainHand == Items.END_CRYSTAL && offHand == Items.GOLDEN_APPLE && event.getHand() == EnumHand.MAIN_HAND) {
                event.setCanceled(true);
                mc.player.setActiveHand(EnumHand.OFF_HAND);
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
            }
        }
    }
}