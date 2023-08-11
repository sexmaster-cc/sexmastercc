package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.logoutspots.LogoutSpots;

public class LogoutSpotsCommand extends Command {
    public LogoutSpotsCommand() {
        super(new String[]{"ClearLogouts", "clearlogs"});
    }

    @Override
    public String execute() {
        final LogoutSpots LOGOUT_SPOTS = Managers.MODULE.get(LogoutSpots.class);
        if (LOGOUT_SPOTS.spots.isEmpty()) {
            return "There are no logout spots";
        }

        LOGOUT_SPOTS.spots.clear();
        return "Cleared logout spots";
    }
}
