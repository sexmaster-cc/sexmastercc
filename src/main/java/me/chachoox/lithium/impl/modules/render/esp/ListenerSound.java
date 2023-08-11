package me.chachoox.lithium.impl.modules.render.esp;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;

public class ListenerSound extends ModuleListener<ESP, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(ESP module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        if (event.getPacket().getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
            module.teleportPos = new BlockPos(
                    event.getPacket().getX(),
                    event.getPacket().getY(),
                    event.getPacket().getZ());
            module.teleportTimer.reset();
        }
    }
}
