package me.chachoox.lithium.impl.command.commands.misc.list;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

import java.util.StringJoiner;

public class ModuleCommand extends Command {
    public ModuleCommand() {
        super(new String[]{"Modules", "mods", "ModuleList", "ModList"});
    }

    @Override
    public String execute() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Managers.MODULE.getModules().forEach(module -> stringJoiner.add(module.getLabel()));
        String message = String.format(String.valueOf(stringJoiner));
        return String.format("Modules (%s): %s", Managers.MODULE.getModules().size(), message);
    }
}
