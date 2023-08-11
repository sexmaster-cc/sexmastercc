package me.chachoox.lithium.impl.modules.misc.chatappend;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketChatMessage;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ListenerChat extends ModuleListener<ChatAppend, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChat(ChatAppend module) {
        super(module, PacketEvent.Send.class, CPacketChatMessage.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketChatMessage> event) {
        ICPacketChatMessage packet = (ICPacketChatMessage) event.getPacket();
        String chatMessage = event.getPacket().getMessage();
        if (ChatAppend.allowMessage(chatMessage)) {
            packet.setMessage(chatMessage + module.mode.getValue().getSuffix());
        }
    }
}
