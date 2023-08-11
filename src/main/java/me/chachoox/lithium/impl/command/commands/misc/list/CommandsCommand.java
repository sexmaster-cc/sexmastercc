package me.chachoox.lithium.impl.command.commands.misc.list;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

import java.util.Arrays;
import java.util.StringJoiner;

public class CommandsCommand extends Command {
    public CommandsCommand() {
        super(new String[]{"Commands", "cmds", "commands"});
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        //noinspection OptionalGetWithoutIsPresent
        Managers.COMMAND.getCommands().forEach(command -> stringJoiner.add(Arrays.stream(command.getAliases()).findFirst().get()));
        return String.format("Commands (%s) %s", Managers.COMMAND.getCommands().size(), stringJoiner);
    }
}
