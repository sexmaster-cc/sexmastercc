package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook extends ModuleListener<Fly, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(Fly module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        if (module.autoDisable.getValue()) {
            module.disable();
        }
    }
}
