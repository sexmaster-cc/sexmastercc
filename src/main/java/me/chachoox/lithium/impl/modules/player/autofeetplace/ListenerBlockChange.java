package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;

public class ListenerBlockChange extends ModuleListener<AutoFeetPlace, PacketEvent.Receive<SPacketBlockChange>> {
    public ListenerBlockChange(AutoFeetPlace module) {
        super(module, PacketEvent.Receive.class, SPacketBlockChange.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketBlockChange> event) {
        BlockPos changePos = event.getPacket().getBlockPosition();
        if (event.getPacket().getBlockState().getMaterial().isReplaceable() && module.getPlacements().contains(changePos)) {
            module.placeBlock(changePos);
        }
    }
}
