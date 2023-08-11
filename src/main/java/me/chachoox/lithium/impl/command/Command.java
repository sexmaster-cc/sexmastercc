package me.chachoox.lithium.impl.command;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.text.TextColor;

import java.util.StringJoiner;

//TODO: find a way so we can use infite arguments
public abstract class Command implements Minecraftable {
    private final String[] aliases;
    private final Argument[] arguments;

    public Command(String[] aliases, Argument ... arguments) {
        this.aliases = aliases;
        this.arguments = arguments;
    }

    public String execute(String[] args) {
        Argument[] arguments = this.getArguments();
        boolean valid = false;
        if (args.length < arguments.length) {
            return String.format("%s %s", args[0], this.getSyntax());
        }
        if (args.length - 1 > arguments.length) {
            return String.format("Maximum number of arguments is %s%s", TextColor.RED, arguments.length);
        }
        if (arguments.length > 0) {
            for (int index = 0; index < arguments.length; ++index) {
                Argument argument = arguments[index];
                argument.setPresent(true);
                argument.setValue(args[index + 1]);
                valid = argument.isPresent();
            }
        } else {
            valid = true;
        }
        return valid ? this.execute() : "Invalid argument(s)";
    }

    public String[] getAliases() {
        return aliases;
    }

    public final Argument[] getArguments() {
        return this.arguments;
    }

    public Argument getArgument(String label) {
        for (Argument argument : arguments) {
            if (!label.equalsIgnoreCase(argument.getLabel())) continue;
            return argument;
        }
        return null;
    }

    public String getSyntax() {
        final StringJoiner stringJoiner = new StringJoiner(" ");
        for (Argument argument : arguments) {
            stringJoiner.add(String.format("[%s]", argument.getLabel()));
        }
        return stringJoiner.toString();
    }

    public abstract String execute();
}
