package me.chachoox.lithium.impl.modules.misc.nobreakanim;

import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public class ListenerPacket extends ModuleListener<NoBreakAnim, PacketEvent.Post<CPacketPlayerDigging>> {
    public ListenerPacket(NoBreakAnim module) {
        super(module, PacketEvent.Post.class, CPacketPlayerDigging.class);
    }

    @Override
    public void call(PacketEvent.Post<CPacketPlayerDigging> event) {
        if (!mc.player.capabilities.isCreativeMode && event.getPacket().getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK && MineUtil.canBreak(event.getPacket().getPosition())) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, event.getPacket().getPosition(), event.getPacket().getFacing()));
        }
    }
}