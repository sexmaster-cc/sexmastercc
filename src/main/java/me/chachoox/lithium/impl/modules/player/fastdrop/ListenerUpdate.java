package me.chachoox.lithium.impl.modules.player.fastdrop;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ListenerUpdate extends ModuleListener<FastDrop, UpdateEvent> {

    public ListenerUpdate(FastDrop module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.isItemValid(mc.player.getHeldItemMainhand().getItem()) && mc.gameSettings.keyBindDrop.isKeyDown()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}

