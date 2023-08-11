package me.chachoox.lithium.impl.modules.movement.velocity;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketEntityVelocity;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class ListenerVelocity extends ModuleListener<Velocity, PacketEvent.Receive<SPacketEntityVelocity>> {
    public ListenerVelocity(Velocity module) {
        super(module, PacketEvent.Receive.class, SPacketEntityVelocity.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketEntityVelocity> event) {
        SPacketEntityVelocity packet = event.getPacket();
        if (mc.player != null) {
            if (packet.getEntityID() == mc.player.getEntityId() && module.horizontal.getValue() != 0 && module.horizontal.getValue() != 0) {
                ((ISPacketEntityVelocity) packet).setX((packet.getMotionX() * module.horizontal.getValue() / 100));
                ((ISPacketEntityVelocity) packet).setY((packet.getMotionY() * module.vertical.getValue() / 100));
                ((ISPacketEntityVelocity) packet).setZ((packet.getMotionZ() * module.horizontal.getValue() / 100));
            } else {
                if (packet.getEntityID() == mc.player.getEntityId()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
