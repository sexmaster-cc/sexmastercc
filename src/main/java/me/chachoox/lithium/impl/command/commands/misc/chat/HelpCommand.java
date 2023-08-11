package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.minecraft.DummyManager;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(new String[]{"Help", "halp", "imretarded"});
    }

    @Override
    public String execute() {
        DummyManager.runTutorial();
        return "Tutorial loaded";
    }
}
