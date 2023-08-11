package me.chachoox.lithium.impl.modules.combat.offhand;

import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ListenerGameLoop extends ModuleListener<Offhand, GameLoopEvent> {
    public ListenerGameLoop(Offhand module) {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void call(GameLoopEvent event) {
        if (mc.player != null && !(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof GuiInventory) {

            if (module.swordGap.getValue()) {
                module.gap = mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword;
            }

            boolean safe = Managers.SAFE.isSafe();

            Item item = module.getItem(safe, module.gap);

            if (module.mainhand.getValue()) {
                module.mainhandTotem(mc.player.inventory.currentItem);
                return; //this return should be removed im just want to use future autototem with this to test shit on 2bpvp
            }

            if (mc.player.getHeldItemOffhand().getItem() != item) {
                int slot = module.getItemSlot(item);
                if (slot != -1) {
                    slot = slot < 9 ? slot + 36 : slot;
                    if (item != Items.TOTEM_OF_UNDYING && !module.timer.passed(350L)) {
                        return;
                    }

                    module.windowClick(slot);
                    module.windowClick(45);
                    module.windowClick(slot);
                    module.timer.reset();
                }
            }
        }
    }
}