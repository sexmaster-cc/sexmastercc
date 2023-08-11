package me.chachoox.lithium.impl.managers.minecraft.chat;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.api.util.text.component.SuppliedComponent;
import me.chachoox.lithium.api.util.thread.events.RunnableClickEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ChatManager extends SubscriberImpl implements Minecraftable {

    private final Set<String> IGNORED_WORDS = new HashSet<>();
    private final Set<String> IGNORED_PLAYERS = new HashSet<>();

    public ChatManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketChat>>(PacketEvent.Receive.class, SPacketChat.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketChat> event) {
                final String msg = event.getPacket().getChatComponent().getUnformattedText();
                for (String string : IGNORED_PLAYERS) { // players
                    if (string != null) {
                        if (msg.toUpperCase().contains(String.format("<%s>", string.toUpperCase()))) {
                            event.setCanceled(true);
                            Logger.getLogger().logNoMark(createClickablePlayerMessage(TextUtil.removeColor(string), msg), true);
                        }
                    }
                }
                for (String string : IGNORED_WORDS) { // words
                    if (string != null) {
                        if (msg.toUpperCase().contains(string.toUpperCase())) {
                            event.setCanceled(true);
                            Logger.getLogger().logNoMark(createClickableWordMessage(TextUtil.removeColor(string), msg), true);
                        }
                    }
                }
            }
        });
    }

    private ITextComponent createClickableWordMessage(String word, String message) {
        AtomicReference<String> supplier = new AtomicReference<>(String.format("%sClick to show hidden message containing word %s.", TextColor.LIGHT_PURPLE, word));
        ITextComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent(new RunnableClickEvent(() -> Logger.getLogger().logNoMark(message, 0x2)));
        return component;
    }

    private ITextComponent createClickablePlayerMessage(String name, String message) {
        AtomicReference<String> supplier = new AtomicReference<>(String.format("%sClick to show hidden message from %s.", TextColor.LIGHT_PURPLE, name));
        ITextComponent component = new SuppliedComponent(supplier::get);
        component.getStyle().setClickEvent(new RunnableClickEvent(
                () -> Logger.getLogger().logNoMark(
                        String.format("[%s]%s", name, message.replace(
                                String.format("<%s>", name), "")), 0x1)));
        return component;
    }

    public Collection<String> getIgnoredWords() {
        return IGNORED_WORDS;
    }

    public Collection<String> getIgnoredPlayers() {
        return IGNORED_PLAYERS;
    }
}