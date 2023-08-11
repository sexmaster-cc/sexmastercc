package me.chachoox.lithium.impl.modules.misc.antiinteract;

import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.item.ItemFood;
import net.minecraft.util.math.BlockPos;

public class ListenerInteract extends ModuleListener<AntiInteract, ClickBlockEvent.Right> {
    public ListenerInteract(AntiInteract module) {
        super(module, ClickBlockEvent.Right.class);
    }

    @Override
    public void call(ClickBlockEvent.Right event) {
        if (module.sneak.getValue() && Managers.ACTION.isSneaking()) {
            return;
        }

        if (module.onlyFood.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemFood)) {
            return;
        }

        BlockPos pos = event.getPos();
        if (module.isValid(mc.world.getBlockState(pos).getBlock())) {
            event.setCanceled(true);
        }
    }
}

