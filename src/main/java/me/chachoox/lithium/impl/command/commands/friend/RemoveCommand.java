package me.chachoox.lithium.impl.command.commands.friend;

import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class RemoveCommand extends Command {
    public RemoveCommand() {
        super(new String[]{"Remove", "rem", "del", "delete"}, new Argument("player"));
    }

    @Override
    public String execute() {
        String name = this.getArgument("player").getValue();
        if (!Managers.FRIEND.isFriend(name)) {
            return "That user is not a friend";
        }
        Managers.FRIEND.removeFriend(name);
        return String.format("Removed %s%s%s as a friend", TextColor.RED, name, TextColor.LIGHT_PURPLE);
    }
}
