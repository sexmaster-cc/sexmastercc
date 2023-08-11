package me.chachoox.lithium.impl.command.commands.misc.refresh;

import me.chachoox.lithium.impl.command.Command;

public class ResourceRefreshCommand extends Command {
    public ResourceRefreshCommand() {
        super(new String[]{"ResourseRefresh", "RefreshResourses", "PackReload"});
    }

    @Override
    public String execute() {
        //noinspection deprecation
        mc.refreshResources();
        return "Textures reloaded";
    }
}
