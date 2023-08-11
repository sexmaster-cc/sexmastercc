package me.chachoox.lithium.impl.modules.misc.chattimestamps;

import me.chachoox.lithium.asm.mixins.network.server.ISPacketChat;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListenerChat extends ModuleListener<ChatTimeStamps, PacketEvent.Receive<SPacketChat>> {
    public ListenerChat(ChatTimeStamps module) {
        super(module, PacketEvent.Receive.class, -5000, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        final SPacketChat packet = event.getPacket();
        final ISPacketChat chatPacket = (ISPacketChat) event.getPacket();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm");
        String timeStamp = simpleDateFormat.format(calendar.getTime());
        String time = module.getTimeStamps(timeStamp);
        chatPacket.setChatComponent(new TextComponentString(time + packet.getChatComponent().getFormattedText()));
    }
}
