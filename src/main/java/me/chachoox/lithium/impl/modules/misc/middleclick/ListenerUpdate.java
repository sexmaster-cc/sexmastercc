package me.chachoox.lithium.impl.modules.misc.middleclick;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class ListenerUpdate extends ModuleListener<MiddleClick, UpdateEvent> {
    public ListenerUpdate(MiddleClick module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (Mouse.isButtonDown(2)) {
            if (!module.clicked && mc.currentScreen == null) {
                if (module.friend.getValue()) {
                    module.onClick();
                }
                if (module.pearl.getValue()) {
                    if (module.onEntity()) {
                        return;
                    }
                    int pearlSlot = ItemUtil.getItemFromHotbar(Items.ENDER_PEARL);
                    if (pearlSlot != -1 || mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL) {
                        int oldSlot = mc.player.inventory.currentItem;
                        if (!(mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL)) {
                            ItemUtil.switchTo(pearlSlot);
                        }
                        mc.playerController.processRightClick(mc.player, mc.world, mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                        if (!(mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL)) {
                            ItemUtil.switchTo(oldSlot);
                        }
                    }
                }
            }
            module.clicked = true;
        } else {
            module.clicked = false;
        }
    }
}
