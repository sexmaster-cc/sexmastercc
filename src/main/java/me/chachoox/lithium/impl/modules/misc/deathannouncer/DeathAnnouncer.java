package me.chachoox.lithium.impl.modules.misc.deathannouncer;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.modules.misc.deathannouncer.util.Announce;

import java.util.Random;

public class DeathAnnouncer extends Module {

    protected final NumberProperty<Integer> range =
            new NumberProperty<>(
                    10, 5, 30,
                    new String[]{"Range", "r", "rangejamin"},
                    "How close we have to be to the death to announce it."
            );

    protected final NumberProperty<Integer> yLevel =
            new NumberProperty<>(
                    50, 1, 255,
                    new String[]{"YLevel", "height", "ylvl"},
                    "How far down we have to be to announce the death."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    5, 0, 15,
                    new String[]{"Delay", "del", "d"},
                    "Delay for sending message to make it look like we typed it."
            );

    protected final EnumProperty<Announce> killSayPreset =
            new EnumProperty<>(
                    Announce.TROLLGOD,
                    new String[]{"Message", "preset", "mode", "type"},
                    "What we want the message to be."
            );

    protected final Property<Boolean> greenText =
            new Property<>(
                    false,
                    new String[]{"GreenText", "greentxt", "greentx"},
                    "Puts > in front of your message to make it green on some servers."
            );

    protected StopWatch timer = new StopWatch();
    protected String name;

    public DeathAnnouncer() {
        super("DeathAnnouncer", new String[]{"DeathAnnouncer", "autogg", "autoez"}, "Announces when a player dies.", Category.MISC);
        this.offerProperties(range, yLevel, delay, killSayPreset, greenText);
        this.offerListeners(new ListenerDeath(this));
    }

    @Override
    public void onDisable() {
        name = null;
    }

    @Override
    public String getSuffix() {
        return killSayPreset.getFixedValue();
    }

    protected String trollGodMessage() {
        return "You got boiled alhamdulillah " + name;
    }

    protected String prayerMessage() {
        return "Mashallah you just got sent to Jahannam " + name;
    }

    protected String auroraMessage() {
        return "GET FUCKED BY AURORA PUSSY " + name;
    }

    protected String autismMessage() {
        return "> " + "EZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ " + name;
    }

    protected String pollosMessage() {
        return "i just raped " + name + " thanks to pollosmod.wtf";
    }

    protected String abyssMessage() {
        return "GG " + name + " Abyss on top!";
    }

    protected String phobosMessage() {
        return name + " is a noob hahaha phobos on tope";
    }
    protected String nftMessage() {
        return "Follow me on rumble https://rumble.com/BitCrypto89 " + name;
    }

    protected String trollHackMessages() {
        String[] konasMessages =
                {
                        "Good fight" + name + "! Troll Hack owns me and all",
                        "gg, " + name,
                        "Troll Hack on top! ez " + name,
                        "You just got ez'd " + name,
                        "You just got naenae'd by Troll Hack, " + name

                };
        return konasMessages[new Random().nextInt(konasMessages.length)];
    }

    protected String kamiMessage() {
        return "KAMI BLUE on top! ez " + name;
    }

    protected String konasMessages() {
        String[] konasMessages =
                {
                        "you just got nae nae'd by konas " + name,
                        name + " tango down",
                        name + " you just felt the wrath of konas client",
                        "I guess konas ca is too fast for you " + name,
                        name + " konas ca is too fast!",
                        "you just got ez'd by konas client " + name

                };
        return konasMessages[new Random().nextInt(konasMessages.length)];
    }

    protected String wurstPlusMessages() {
        String[] wurstPlusMessages =
                {
                        "you just got nae nae'd by wurst+2 ",
                        "you just got nae nae'd by wurst+2 - discord.gg/wurst",
                        "you just got nae nae'd by wurst+3",
                        "you just got nae nae'd by wurst+3 | discord.gg/wurst"
                };
        return wurstPlusMessages[new Random().nextInt(wurstPlusMessages.length)];
    }

    protected String wellPlayedMessages() {
        String[] wellPlayedMessages =
                {
                        "wp",
                        name + " wp",
                        "well played",
                        name + " well played"

                };
        return wellPlayedMessages[new Random().nextInt(wellPlayedMessages.length)];
    }
}
