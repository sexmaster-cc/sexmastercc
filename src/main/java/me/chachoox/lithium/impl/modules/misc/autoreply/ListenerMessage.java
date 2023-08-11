package me.chachoox.lithium.impl.modules.misc.autoreply;

import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.network.play.server.SPacketChat;

import java.util.stream.Stream;

public class ListenerMessage extends ModuleListener<AutoReply, PacketEvent.Receive<SPacketChat>> {
    public ListenerMessage(AutoReply module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        if (!module.timer.passed(module.delay.getValue() * 1000)) {
            return;
        }

        SPacketChat packet = event.getPacket();
        String formattedText = packet.getChatComponent().getFormattedText();
        String message = packet.getChatComponent().getUnformattedText();

        String ign =  message.split(" ")[0];

       if (Stream.of("whispers:", "whispers to you:", "says:").anyMatch(message::contains)
               && message.contains(ign)
               && Stream.of(TextColor.WHITE).anyMatch(s -> !formattedText.contains(s))
               && !ign.equals(mc.player.getName())) {

           sendReply(ign, message);
       }
    }

    private void sendReply(String name, String message) {
        final String reply;

        if (Managers.FRIEND.isFriend(name)
                && mc.player.posX > 5000
                && mc.player.posZ > 5000
                && module.message.getValue() == ReplyMessages.COORDS
                && Stream.of("coords", "coord", "wya").anyMatch(message::contains)) {
            reply = getCoords();
        } else {
            reply = module.message.getValue().getReply();
        }

        if (reply.equalsIgnoreCase("<Coords>")) {
            return;
        }

        mc.player.sendChatMessage("/r " + reply);
        module.timer.reset();
    }

    private String getCoords() {
        return "XYZ: "
                + (int) mc.player.posX + ", "
                + (int) mc.player.posY + ", "
                + (int) mc.player.posZ;
    }
}
