package me.chachoox.lithium.impl.command.commands.misc.chat.ignore;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class IgnoreCommand extends Command {
    public IgnoreCommand() {
        super(new String[]{"Ignore", "Hide", "KILL"}, new Argument("player"));
    }

    @Override
    public String execute() {
        final String plr = this.getArgument("player").getValue();
        if (Managers.CHAT.getIgnoredPlayers().contains(plr)) {
            return String.format("[%s] is already in the list", plr);
        }
        Managers.CHAT.getIgnoredPlayers().add(String.format("%s", plr));
        return String.format("Ignored [%s]", plr);
    }
}
