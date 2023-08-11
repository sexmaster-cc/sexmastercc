package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Command;

public class ShrugCommand extends Command {
    public ShrugCommand() {
        super(new String[]{"Shrug", "AnnoyingAssFace"});
    }

    @Override
    public String execute() {
        mc.player.sendChatMessage("\u00AF\\_(\u30C4)_/\u00AF");
        return "Shrugging";
    }
}
