package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class SexCommand extends Command {
    public SexCommand() {
        super(new String[]{"Fuck", "sex"}, new Argument("bottom"), new Argument("top"));
    }

    @Override
    public String execute() {
        return String.format("%s and %s are now fucking", this.getArgument("bottom"), this.getArgument("top"));
    }
}
