package me.chachoox.lithium.impl.modules.render.betterchat;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.modules.render.betterchat.util.ChatType;

public class BetterChat extends Module {

    private final NumberProperty<Float> alphaFactor =
            new NumberProperty<>(
                    0.5F, 0.0F, 1.0F, 0.01F,
                    new String[]{"RectAlpha", "chatbackground"},
                    "Changes the alpha of the chat rectangle, default is the vanilla one."
            );

    private final EnumProperty<ColorEnum> selfColor =
            new EnumProperty<>(
                    ColorEnum.NONE,
                    new String[]{"Highlight", "playercolor"},
                    ColorEnum.COLOR_DESC
            );

    private final Property<Boolean> soundOnHighlight =
            new Property<>(
                    false,
                    new String[]{"SoundOnHighlight", "HighlightSound", "Notification"},
                    "Plays a sound whenever someone says your name in chat."
            );

    protected final Property<Boolean> lowercase =
            new Property<>(
                    false,
                    new String[]{"Lowercase", "Anticringe", "Anitannoying", "hiedunfunnyassniggas"},
                    "Makes all chat messages lowercase."
            );


    private final Property<Boolean> blur =
            new Property<>(
                    false,
                    new String[]{"Blur", "cum"},
                    "Adds cum when you open chat."
            );

    private final Property<Boolean> infinite =
            new Property<>(
                    false,
                    new String[]{"Infinite", "infi"},
                    "Removes chat limit."
            );

    protected final EnumProperty<ChatType> chatType =
            new EnumProperty<>(ChatType.NONE,
                    new String[]{"ChatType", "fancychat"},
                    "Changes how your message will look."
            );

    protected final Property<Boolean> period =
            new Property<>(
                    false,
                    new String[]{"Period", "dot"},
                    "Ends all of your messages with a period, -> (cpvpnn is a gorilla.)."
            );

    protected final Property<Boolean> greenText =
            new Property<>(
                    false,
                    new String[]{"Green", "Text"},
                    "Starts your messages with >, -> (> cpvpnn is a gorilla.)."
            );

    protected final Property<Boolean> face =
            new Property<>(
                    false,
                    new String[]{"Face", "cute"},
                    "Starts your messages with a face and a heart and ends it with a heart, -> (\u3063\u25D4\u25E1\u25D4)\u3063 \u2665 cpvpnn is a gorilla \u2665)."
            );

    protected final Property<Boolean> angry =
            new Property<>(
                    false,
                    new String[]{"Angry", "Screaming", "NidzyoWhenFriendlessAssNiggas"},
                    "MAKES YOUR TEXT ALL UPPERCASE."
            );

    protected final Property<Boolean> antiKick =
            new Property<>(
                    false,
                    new String[]{"AntiKick", "AntiCensor", "NoKick"},
                    "Puts a random string of characters at the end of your messages -> | f3j0f3."
            );

    protected final Property<Boolean> whispers =
            new Property<>(
                    false,
                    new String[]{"Whispers", "AntiKickWhisper", "NoKickWhisper"},
                    "If you want to use AntiKick when whispering."
            );

    public BetterChat() {
        super("BetterChat", new String[]{"BetterChat", "ChatTweaks", "ChatModify", "NoChatRect"}, "Tweaks minecraft's chat.", Category.RENDER);
        this.offerListeners(new ListenerChatSend(this), new ListenerChatRecieve(this));
        this.offerProperties(alphaFactor, selfColor, soundOnHighlight, lowercase, blur, infinite, chatType, period, greenText, face, angry, antiKick, whispers);
    }

    protected boolean allowMessage(String message) {
        String[] filters = new String[]{"/", ".", ",", "$", "#", "+", "@", "!", "*", "-"};
        boolean allow = true;
        for (String s : filters) {
            if (whispers.getValue() && s.equals("/")) {
                continue;
            }
            if (!message.startsWith(s)) continue;
            allow = false;
            break;
        }
        return allow;
    }


    public boolean drawBlur() {
        return isEnabled() && blur.getValue();
    }

    public boolean noAnnoyingPeople() {
        return isEnabled() && lowercase.getValue();
    }

    public boolean isInfinite() {
        return isEnabled() && infinite.getValue();
    }

    public boolean playSoundOnHighlight() {
        return isEnabled() && soundOnHighlight.getValue();
    }

    public float getRectAlpha() {
        return alphaFactor.getValue();
    }

    public String getPlayerColor() {
        return selfColor.getValue().getColor();
    }

}
