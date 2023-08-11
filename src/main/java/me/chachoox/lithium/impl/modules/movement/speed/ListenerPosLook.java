package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook extends ModuleListener<Speed, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(Speed module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        module.distance = 0.0;
        module.speed = 0.0;
        module.strafeStage = 4;
        module.onGroundStage = 2;
    }
}
