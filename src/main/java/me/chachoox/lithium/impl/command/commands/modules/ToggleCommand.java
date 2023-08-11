package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.module.PersistentModule;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super(new String[]{"Toggle", "t"}, new Argument("module"));
    }

    @Override
    public String execute() {
        Module module = Managers.MODULE.getModuleByAlias(this.getArgument("module").getValue());

        if (module == null) {
            return "No such module exists";
        }

        if (module instanceof PersistentModule) {
            return "Module is persistent";
        }

        mc.addScheduledTask(module::toggle);
        return String.format("%s has been toggled %s", module.getLabel(), module.isEnabled() ? "on." : "off.");
    }
}
