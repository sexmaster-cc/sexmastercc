package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ChatFilterCommand extends Command {
    public ChatFilterCommand() {
        super(new String[]{"ChatFilter", "filter", "chathide"}, new Argument("add/del"), new Argument("word"));
    }

    @Override
    public String execute() {
        final String wrd = this.getArgument("word").getValue();
        final String arg = this.getArgument("add/del").getValue();
        switch (arg.toUpperCase()) {
            case ("ADD"):
                if (Managers.CHAT.getIgnoredWords().contains(wrd)) {
                    return String.format("[%s] is already filtered", wrd);
                }
                Managers.CHAT.getIgnoredWords().add(TextUtil.formatString(wrd));
                return String.format("Filtered word [%s]", wrd);
            case ("DEL"):
            case ("DELETE"):
            case ("REMOVE"):
                Managers.CHAT.getIgnoredWords().remove(TextUtil.formatString(wrd));
                return String.format("Unfiltered word [%s]", wrd);
        }
        return this.getSyntax();
    }
}
