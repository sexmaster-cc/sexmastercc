package me.chachoox.lithium.impl.modules.render.betterchat;

import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketChatMessage;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.chatappend.ChatAppend;
import me.chachoox.lithium.impl.modules.render.betterchat.util.ChatType;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.stream.Stream;

public class ListenerChatSend extends ModuleListener<BetterChat, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerChatSend(BetterChat module) {
        super(module, PacketEvent.Send.class, 500, CPacketChatMessage.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketChatMessage> event) {
        ICPacketChatMessage packet = (ICPacketChatMessage) event.getPacket();
        String chatMessage = event.getPacket().getMessage();

        if (ChatAppend.allowMessage(chatMessage)) {
            if (module.angry.getValue()) {
                chatMessage = chatMessage.toUpperCase();
            }

            if (module.chatType.getValue() == ChatType.L33T) {
                if (Stream.of("o", "l", "a", "e", "i", "s").anyMatch(chatMessage::contains)) {
                    chatMessage = chatMessage
                            .replace("o", "0")
                            .replace("l", "1")
                            .replace("a", "4")
                            .replace("e", "3")
                            .replace("i", "!")
                            .replace("s", "$");
                }
            }

            if (module.chatType.getValue() == ChatType.DERP) {
                char[] array = chatMessage.toCharArray();
                int i = 0;
                while (i < chatMessage.length()) {
                    final char character;
                    if (Character.isUpperCase(array[i])) {
                        character = Character.toLowerCase(array[i]);
                    } else {
                        character = Character.toUpperCase(array[i]);
                    }
                    array[i] = character;
                    i += 2;
                }

                chatMessage = String.valueOf(array);
            }

            if (module.chatType.getValue() == ChatType.REVERSE) {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(chatMessage);
                chatMessage = stringBuilder.reverse().toString();
            }

            if (module.period.getValue()) {
                chatMessage = chatMessage + ".";
            }

            if (module.chatType.getValue() == ChatType.FANCY) {
                final StringBuilder stringBuilder = new StringBuilder();
                char[] array;
                int length = (array = chatMessage.toCharArray()).length;
                int i = 0;
                while (i < length) {
                    final char character = array[i];
                    if (character >= '!' && character <= '\u0080' && !"(){}[]|".contains(Character.toString(character))) {
                        stringBuilder.append(new String(Character.toChars(character + '\ufee0')));
                    } else {
                        stringBuilder.append(character);
                    }
                    ++i;
                }

                chatMessage = stringBuilder.toString();
            }

            if (module.face.getValue()) {
                chatMessage = String.format("(\u3063\u25D4\u25E1\u25D4)\u3063 \u2665 %s \u2665", chatMessage);
            }

            if (module.face.getValue()) {
                chatMessage = String.format("> %s", chatMessage);
            }
        }

        if (module.antiKick.getValue() && module.allowMessage(chatMessage)) {
            final String antiKickString = module.antiKick.getValue() ? TextUtil.generateRandomString(5) : "";
            chatMessage = chatMessage + antiKickString;
        }

        packet.setMessage(chatMessage);
    }
}
