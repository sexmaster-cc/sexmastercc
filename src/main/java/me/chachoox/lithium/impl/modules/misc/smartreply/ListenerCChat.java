package me.chachoox.lithium.impl.modules.misc.smartreply;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketChatMessage;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ListenerCChat extends ModuleListener<SmartReply, PacketEvent.Send<CPacketChatMessage>> {
    public ListenerCChat(SmartReply module) {
        super(module, PacketEvent.Send.class, CPacketChatMessage.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketChatMessage> event) {
        CPacketChatMessage packet = event.getPacket();
        String message = packet.getMessage();
        String input = message.split(" ")[0];
        boolean isSlashR = input.equalsIgnoreCase("/r");

        if (!module.whispered && isSlashR) {
            Logger.getLogger().log("<SmartReply> You have not been whispered yet");
            event.setCanceled(true);
            return;
        }

        if (module.whisperSender != null && isSlashR) {
            ((ICPacketChatMessage) packet).setMessage("/msg " + module.whisperSender + " " + message.substring(3));
        }
    }
}
