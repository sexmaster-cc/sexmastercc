package me.chachoox.lithium.impl.modules.render.betterchat;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChat;

public class ListenerChatRecieve extends ModuleListener<BetterChat, PacketEvent.Receive<SPacketChat>> {
    public ListenerChatRecieve(BetterChat module) {
        super(module, PacketEvent.Receive.class, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        final SPacketChat packet = event.getPacket();
        if (mc.player != null && module.playSoundOnHighlight() && packet.getChatComponent().getUnformattedText().contains(mc.player.getName())) {
            mc.player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
        }
    }
}
