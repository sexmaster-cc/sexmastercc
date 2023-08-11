package me.chachoox.lithium.impl.command.commands.values;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;

public class SubscriberCommand extends Command {
    public SubscriberCommand() {
        super(new String[]{"Subscriber", "subs"}, new Argument("module"));
    }

    @Override
    public String execute() {
        Module module = Managers.MODULE.getModuleByAlias(this.getArgument("module").getValue());

        if (module == null) {
            return "No such module exists";
        }

        boolean subscribed = Bus.EVENT_BUS.isSubscribed(module);
        return module.getLabel() + (subscribed ?  " has a subscriber" : " does not have a subscriber");
    }
}
