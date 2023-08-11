package me.chachoox.lithium.impl.command.commands.misc.chat.ignore;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class UnIgnoreCommand extends Command {
    public UnIgnoreCommand() {
        super(new String[]{"Unignore", "unhide", "revive"}, new Argument("player"));
    }

    @Override
    public String execute() {
        final String plr = this.getArgument("player").getValue();
        if (!Managers.CHAT.getIgnoredPlayers().contains(plr)) {
            return String.format("[%s] is not ignored", plr);
        }
        Managers.CHAT.getIgnoredPlayers().remove(String.format("%s", plr));
        return String.format("Unignored [%s]", plr);
    }
}
