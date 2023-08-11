package me.chachoox.lithium.impl.modules.misc.payloadspoof;

import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCounted;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketCustomPayload;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ListenerCustomPayload extends ModuleListener<PayloadSpoof, PacketEvent.Send<CPacketCustomPayload>> {
    public ListenerCustomPayload(PayloadSpoof module) {
        super(module, PacketEvent.Send.class, CPacketCustomPayload.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketCustomPayload> event) {
        if (!mc.isIntegratedServerRunning()) {
            CPacketCustomPayload packet = event.getPacket();
            if (packet.getChannelName().equals("MC|Brand")) {
                PacketBuffer buffer = packet.getBufferData();
                releaseBuffer(buffer);
                ((ICPacketCustomPayload) packet).setData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
            }
        }
    }

    private void releaseBuffer(ReferenceCounted buffer) {
        buffer.release(buffer.refCnt());
    }
}
