package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockChange extends ModuleListener<FastBreak, PacketEvent.Receive<SPacketBlockChange>> {
    public ListenerBlockChange(FastBreak module) {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketBlockChange> event) {
        SPacketBlockChange packet = event.getPacket();
        BlockPos packetPos = packet.getBlockPosition();

        if (packetPos.equals(module.pos) && packet.getBlockState().getBlock() == Blocks.AIR) {
            module.retries = 0;
            module.resetSwap();
        }

        if (packet.getBlockPosition().equals(module.pos) && packet.getBlockState() == mc.world.getBlockState(module.pos) && module.shouldAbort && module.mode.getValue() == MineMode.INSTANT) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, module.pos, EnumFacing.DOWN));
            module.shouldAbort = false;
        }
    }
}
