package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.impl.event.events.misc.RightClickItemEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBow;

public class ListenerUseItem extends ModuleListener<Quiver, RightClickItemEvent>
{
    public ListenerUseItem(Quiver module)
    {
        super(module, RightClickItemEvent.class);
    }

    @Override
    public void call(RightClickItemEvent event) {
        if (mc.player.getHeldItem(event.getHand()).getItem() instanceof ItemBow
                && module.cancelTime.getValue() != 0
                && !module.timer.passed(module.cancelTime.getValue())
                && module.fast) {
            event.setCanceled(true);
        }
    }

}
