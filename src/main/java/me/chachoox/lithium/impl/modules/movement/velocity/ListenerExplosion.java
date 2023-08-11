package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketExplosion;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketExplosion;

public class ListenerExplosion extends ModuleListener<Velocity, PacketEvent.Receive<SPacketExplosion>> {
    public ListenerExplosion(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketExplosion.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketExplosion> event) {
        SPacketExplosion packet = event.getPacket();
        if (mc.player != null) {
            if (module.horizontal.getValue() != 0 && module.vertical.getValue() != 0) {
                ((ISPacketExplosion) packet).setMotionX((packet.getMotionX() * module.horizontal.getValue() / 100));
                ((ISPacketExplosion) packet).setMotionY((packet.getMotionY() * module.vertical.getValue() / 100));
                ((ISPacketExplosion) packet).setMotionZ((packet.getMotionZ() * module.horizontal.getValue() / 100));
            } else {
                event.setCanceled(true);
            }
        }
    }
}
