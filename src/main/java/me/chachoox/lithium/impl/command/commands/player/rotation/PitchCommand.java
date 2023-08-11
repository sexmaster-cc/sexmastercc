package me.chachoox.lithium.impl.command.commands.player.rotation;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

public class PitchCommand extends Command {
    public PitchCommand() {
        super(new String[]{"Pitch", "p"}, new Argument("pitch"));
    }

    @Override
    public String execute() {
        float pitch = Float.parseFloat(this.getArgument("pitch").getValue());
        mc.player.rotationPitch = pitch;
        return "Set pitch to " + pitch;
    }
}

