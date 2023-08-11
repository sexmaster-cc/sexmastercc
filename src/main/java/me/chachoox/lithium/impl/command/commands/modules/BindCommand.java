package me.chachoox.lithium.impl.command.commands.modules;

import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
    public BindCommand() {
        super(new String[]{"Bind", "KeyBind"}, new Argument("module"), new Argument("key"));
    }

    @Override
    public String execute() {
        final Module module = Managers.MODULE.getModuleByLabel(this.getArgument("module").getValue());
        int key = Keyboard.getKeyIndex(this.getArgument("key").getValue().toUpperCase());

        if (module == null) {
            return "No such module exists";
        }

        module.bind.setValue(new Bind(key));// bro pasted da exact message from future (exeter)
        return String.format("%s has been bound to %s", module.getLabel(), Keyboard.getKeyName(key).toUpperCase());
    }
}
