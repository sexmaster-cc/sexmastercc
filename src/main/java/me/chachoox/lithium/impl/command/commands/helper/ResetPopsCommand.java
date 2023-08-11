package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ResetPopsCommand extends Command {
    public ResetPopsCommand() {
        super(new String[]{"ResetPops", "PopsClear", "Clear", "ClearPops"});
    }

    @Override
    public String execute() {
        Managers.TOTEM.getPopMap().clear();
        return "Pops Cleared";
    }
}
