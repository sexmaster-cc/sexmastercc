package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;

public class VelocityPercentageCommand extends Command {
    public VelocityPercentageCommand() {
        super(new String[]{"VelocityPercentage", "Velo%", "VelocityPer"}, new Argument("percentage"));
    }

    @Override
    public String execute() {
        final Velocity VELOCITY = Managers.MODULE.get(Velocity.class);
        int percent = Integer.parseInt(this.getArgument("percentage").getValue());
        VELOCITY.setVelocity(percent, percent);
        return String.format("Set horizontal and vertical velocity to %s", percent);
    }
}
