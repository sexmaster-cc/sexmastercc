package me.chachoox.lithium.impl.modules.misc.chattimestamps;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.api.util.text.TextColor;

/**
 * @author moneymaker552
 */
public class ChatTimeStamps extends Module {

    private final EnumProperty<ColorEnum> bracketColor =
            new EnumProperty<>(
                    ColorEnum.DARKPURPLE,
                    new String[]{"BracketColor", "brackc", "colorbracket", "bc"},
                    "The color of the brackets."
            );

    private final EnumProperty<ColorEnum> timeColor =
            new EnumProperty<>(
                    ColorEnum.LIGHTPURPLE,
                    new String[]{"TimeColor", "timec", "Tc"},
                    "The color of the time."
            );

    private final EnumProperty<TimeStampsBracket> brackets =
            new EnumProperty<>(
                    TimeStampsBracket.BRACKET,
                    new String[]{"Bracket", "b"},
                    "Caret: - < | > / Bracket: - [ | ]."
            );


    protected final Property<Boolean> underline =
            new Property<>(
                    false,
                    new String[]{"Underline", "line",
                            "Add a underline to the text."}
            );

    public ChatTimeStamps() {
        super("ChatTimeStamps", new String[]{"ChatTimeStamps", "chattime", "timestamps"}, "Chat time stamps.", Category.MISC);
        this.offerProperties(bracketColor, timeColor, brackets, underline);
        this.offerListeners(new ListenerChat(this));
    }

    public String getTimeStamps(String time) {
        if (underline.getValue()) {
            time = TextColor.UNDERLINE + time;
        }

        switch (brackets.getValue()) {
            case NONE: {
                return String.format("%s%s%s ",
                        timeColor.getValue().getColor(),
                        time,
                        TextColor.RESET);
            }
            case CARET: {
                return String.format("%s<%s%s%s>%s ",
                        bracketColor.getValue().getColor(),
                        timeColor.getValue().getColor(),
                        time,
                        bracketColor.getValue().getColor(),
                        TextColor.RESET);
            }
            case BRACKET: {
                return String.format("%s[%s%s%s]%s ",
                        bracketColor.getValue().getColor(),
                        timeColor.getValue().getColor(),
                        time,
                        bracketColor.getValue().getColor(),
                        TextColor.RESET);
            }
            default: {
                return "i count monye right";
            }
        }
    }
}
