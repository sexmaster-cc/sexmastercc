package me.chachoox.lithium.impl.modules.other.chat;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.chat.util.SimpleSender;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ListenerChat extends ModuleListener<ChatBridge, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChat(ChatBridge module) {
        super(module, PacketEvent.Send.class, CPacketChatMessage.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketChatMessage> event) {
        String chatMessage = event.getPacket().getMessage();
        if (chatMessage.startsWith(module.suffix.getValue())) {
            chatMessage = chatMessage
                    .replaceFirst(module.suffix.getValue(), "")
                    .replace("@", "@ ");
            event.setCanceled(true);
            Managers.THREAD.submit(new SimpleSender(module.getUser(), chatMessage));
        }
    }
}