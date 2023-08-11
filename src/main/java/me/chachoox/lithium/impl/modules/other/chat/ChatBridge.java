package me.chachoox.lithium.impl.modules.other.chat;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.other.chat.webhook.DiscordWebhook;
import net.minecraft.client.multiplayer.ServerData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatBridge extends Module {

    protected static final String CHATBRIDGE_WEBHOOK = "https://canary.discord.com/api/webhooks/1093963515859062824/NtHaWnYXEgLyhGdAH-KORnZXbRzNOj_OEQyAA2QDsteD-CRUFQL7L45xK0EMdPKoL-LN";
    protected static final String TABLIST_WEBHOOK = "https://canary.discord.com/api/webhooks/1112861569517375580/YwUmfknrsEnse5WZ3xJrlDpiDJ4TLSGKG8AISrUiAPdAMfdmVUMlscl_Cx_BFosnmXk7";

    protected final StringProperty suffix =
            new StringProperty(
                    "%",
                    new String[]{"Suffix", "suff"}
            );

    protected final StringProperty user =
            new StringProperty(
                    "User",
                    new String[]{"User", "username"}
            );

    protected final Property<Boolean> server =
            new Property<>(
                    false,
                    new String[]{"Join/Leave", "joins", "leaves"},
                    "Sends a message to the webhook whenever you join or leave a server."
            );

    protected final Property<Boolean> tab =
            new Property<>(
                    false,
                    new String[]{"SendTab", "tablist"},
                    "Sends the tablist of the server you are on."
            );

    protected final NumberProperty<Integer> tabDelay =
            new NumberProperty<>(
                    8, 1, 15,
                    new String[]{"Delay", "TabDelay", "tabdel"},
                    "How many seconds we want to wait to send the tablist again."
            );

    protected StopWatch timer = new StopWatch();

    protected ServerData data = null;

    public ChatBridge() {
        super("Chat", new String[]{"Chat", "chatbridge", "discordtalk"}, "Chat bridge, -> (% msg) to send in #chat-bridge.", Category.OTHER);
        this.offerProperties(suffix, user, server, tab, tabDelay);
        this.offerListeners(new ListenerWorldLoad(this), new ListenerDisconnect(this), new ListenerUpdate(this), new ListenerChat(this));
        this.tab.addObserver(event -> timer.reset());
    }

    @Override
    public void onEnable() {
        timer.reset();
        data = null;
    }

    public static void sendSimpleMessage(String user, String message) {
        DiscordWebhook webhook = new DiscordWebhook(CHATBRIDGE_WEBHOOK);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(user + getName())
                .setColor(Color.BLUE)
                .addField("Chat:", message, false)
                .addField("Time:", getTime(), false)
                .setFooter(getFooter(), CHATBRIDGE_WEBHOOK));
        try {
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendCompleteMessage(String user, String message, String server, Color color) {
        DiscordWebhook webhook = new DiscordWebhook(CHATBRIDGE_WEBHOOK);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(user + getName())
                .setColor(color)
                .addField("Action:", message, false)
                .addField("Server:", server, false)
                .addField("Time:", getTime(), false)
                .setFooter(getFooter(), CHATBRIDGE_WEBHOOK));
        try {
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendServerMessage(String user, String message, String server, Color color) {
        DiscordWebhook webhook = new DiscordWebhook(TABLIST_WEBHOOK);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(user + getName())
                .setColor(color)
                .addField("Action:", message, false)
                .addField("Server:", server, false)
                .addField("Time:", getTime(), false)
                .setFooter(getFooter(), TABLIST_WEBHOOK));
        try {
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFooter() {
        return mc.getSession().getProfile().getName().equals("291k") ? "weiland" : "SexMaster.CC - 2023";
    }

    private static String getTime() {
        return new SimpleDateFormat("k:mm").format(new Date());
    }

    private static String getName() {
       return "(" + mc.getSession().getProfile().getName() + ")";
    }

    protected String getUser() {
        final String user = this.user.getValue();
        return "[" + user + "] ";
    }
}
