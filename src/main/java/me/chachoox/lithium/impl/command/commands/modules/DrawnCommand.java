package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class DrawnCommand extends Command {
    public DrawnCommand() {
        super(new String[]{"Drawn", "Hide", "Show"}, new Argument("module"));
    }

    @Override
    public String execute() {
        Module module = Managers.MODULE.getModuleByAlias(this.getArgument("module").getValue());

        if (module == null) {
            return "No such module exists";
        }

        module.setDrawn(!module.drawn.getValue());
        return module.getLabel() + " has been " + (module.isHidden() ? "hidden" : "unhidden");
    }
}

