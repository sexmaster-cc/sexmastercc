package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super(new String[]{"Prefix", "pref", "p"}, new Argument("char"));
    }

    @Override
    public String execute() {
        final String prefix = this.getArgument("char").getValue();
        if (prefix.equalsIgnoreCase(Managers.COMMAND.getPrefix())) {
            return "That is already your prefix";
        }

        Managers.COMMAND.setPrefix(prefix);
        return String.format("%s is now your prefix", prefix);
    }
}
