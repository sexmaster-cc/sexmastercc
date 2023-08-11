package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class DrawnAllCommand extends Command {
    public DrawnAllCommand() {
        super(new String[]{"DrawnAll", "DrawAll"});
    }

    @Override
    public String execute() {
        for (Module module : Managers.MODULE.getModules()) {
            module.setDrawn(!module.drawn.getValue());
        }
        return String.format("Drawn %s modules", Managers.MODULE.getModules().size());
    }
}
