package me.chachoox.lithium.impl.modules.player.autofeetplace;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;

public class ListenerSound extends ModuleListener<AutoFeetPlace, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(AutoFeetPlace module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        BlockPos explosion = new BlockPos(event.getPacket().getX(), event.getPacket().getY(), event.getPacket().getZ());
        if (module.getPlacements().contains(explosion)) {
            module.placeBlock(explosion);
        }
    }
}
