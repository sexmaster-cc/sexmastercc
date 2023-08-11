package me.chachoox.lithium.impl.command.commands.friend;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

import java.util.StringJoiner;

public class FriendsCommand extends Command {
    public FriendsCommand() {
        super(new String[]{"Friends", "friendlist"});
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Managers.FRIEND.getFriends().forEach(module -> stringJoiner.add(module.getLabel()));
        String message = String.format(String.valueOf(stringJoiner));
        return String.format("Friends (%s): %s", Managers.FRIEND.getFriends().size(), message);
    }
}
