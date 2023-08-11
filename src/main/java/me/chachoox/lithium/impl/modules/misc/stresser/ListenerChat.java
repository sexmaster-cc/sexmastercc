package me.chachoox.lithium.impl.modules.misc.stresser;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.api.util.text.component.SuppliedComponent;
import me.chachoox.lithium.api.util.thread.events.RunnableClickEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;

import java.util.concurrent.atomic.AtomicReference;

public class ListenerChat extends ModuleListener<Stresser, PacketEvent.Receive<SPacketChat>> {
    public ListenerChat(Stresser module) {
        super(module, PacketEvent.Receive.class, 100, SPacketChat.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketChat> event) {
        String text = event.getPacket().getChatComponent().getUnformattedText();
        int count = 0;
        boolean isUnicode = false;
        for (char charset : text.toCharArray()) {
            if (module.antiUnicode.getValue() && !isAscii(charset)) {
                count++;
            }
        }

        if (count >= module.maxUnicodes.getValue()) {
            isUnicode = true;
            event.setCanceled(true);
        }

        if (isUnicode) {
            Logger.getLogger().logNoMark(makeClickableMessage(TextUtil.removeColor(text), count), false);
        }

    }

    private ITextComponent makeClickableMessage(String text, int count) {
        AtomicReference<String> supplier = new AtomicReference<>(TextColor.RED + String.format("<Stresser> Message with %s unicode characters detected.", count));
        ITextComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent(new RunnableClickEvent(() -> Logger.getLogger().log(TextColor.YELLOW + "<Stresser> " + text)));
        return component;
    }

    private boolean isAscii(char ch) {
        return ch < 256;
    }
}
