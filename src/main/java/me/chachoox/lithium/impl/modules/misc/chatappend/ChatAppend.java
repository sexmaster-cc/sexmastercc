package me.chachoox.lithium.impl.modules.misc.chatappend;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.impl.modules.misc.chatappend.util.Suffix;

public class ChatAppend extends Module {

    public final static String[] FILTERS = new String[]{"/", ".", ",", "$", "#", "+", "@", "!", "*", "-"};

    protected EnumProperty<Suffix> mode =
            new EnumProperty<>(
                    Suffix.KONAS,
                    new String[]{"Suffix", "Mode", "Type"},
                    "The type of suffix we will put at the end of the message."
            );

    public ChatAppend() {
        super("ChatAppend", new  String[]{"ChatAppend", "chatsuffix", "suffix"}, "Places a watermark at the end of your messages.", Category.MISC);
        this.offerProperties(mode);
        this.offerListeners(new ListenerChat(this));
        // hope you get executed for that comment
    }

    public static boolean allowMessage(String message) {
        boolean allow = true;
        for (String s : FILTERS) {
            if (!message.startsWith(s)) continue;
            allow = false;
            break;
        }
        return allow;
    }
}
