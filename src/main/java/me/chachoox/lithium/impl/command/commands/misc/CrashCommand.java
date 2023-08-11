package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.impl.command.Command;
import net.minecraft.crash.CrashReport;

public class CrashCommand extends Command {
    public CrashCommand() {
        super(new String[]{"Crash", "FuckJayMoney"});
    }

    @Override
    public String execute() {
        mc.crashed(new CrashReport(">.<", new Throwable()));
        return "Crashing...";
    }
}
