package me.chachoox.lithium.impl.modules.misc.smartreply;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketChat;

public class ListenerSChat extends ModuleListener<SmartReply, PacketEvent.Receive<SPacketChat>> {
    public ListenerSChat(SmartReply module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        String message = event.getPacket().getChatComponent().getUnformattedText();

        if (message.contains("whispers: ") || message.contains("Whispers: ")) {
            if (containsUnicode(message)) {
                return;
            }

            module.whisperSender = message.split(" ")[0];
            module.whispered = true;
        }

    }

    public boolean containsUnicode(String text) {
        for (char charset : text.toCharArray()) {
            if (!isAscii(charset)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAscii(char ch) {
        return ch < 256;
    }
}
