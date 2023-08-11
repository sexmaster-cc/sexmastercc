package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.util.Session;

public class SessionCommand extends Command {
    public SessionCommand() {
        super(new String[]{"Session", "hackermode"}, new Argument("name"), new Argument("uuid"), new Argument("token"));
    }

    @Override
    public String execute() {
        ((IMinecraft) mc).setSession(new Session(this.getArgument("name").getValue(), this.getArgument("uuid").getValue(), this.getArgument("token").getValue(), "mojang"));
        return "Changed session";
    }
}
