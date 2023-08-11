package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class ListenerPosLook extends ModuleListener<FakeLag, PacketEvent.Receive<SPacketPlayerPosLook>> {
    public ListenerPosLook(FakeLag module) {
        super(module, PacketEvent.Receive.class, SPacketPlayerPosLook.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
        module.disable();
    }
}
