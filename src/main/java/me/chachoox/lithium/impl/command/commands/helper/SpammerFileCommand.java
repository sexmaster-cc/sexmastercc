package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.spammer.Spammer;

import java.io.File;
import java.util.Objects;

public class SpammerFileCommand extends Command {

    public SpammerFileCommand() {
        super(new String[]{"SpammerFile", "file", "spamfile", "setfile"}, new Argument("file"));
    }

    @Override
    public String execute() {
        Spammer SPAMMER_MODULE = Managers.MODULE.get(Spammer.class);
        String fileArgument = this.getArgument("File").getValue();

        if (!Lithium.SPAMMER.exists()) {
            return "Spammer directory doesn't exist (" + (Lithium.SPAMMER.mkdir() ? "created new one" :  "failed to create new one") + ")";
        }

        for (File file : Objects.requireNonNull(Lithium.SPAMMER.listFiles())) {
            if (file.getName().startsWith(fileArgument)) {
                SPAMMER_MODULE.setCurrentFile(file);
                if (!SPAMMER_MODULE.isEnabled()) {
                    SPAMMER_MODULE.setEnabled(true);
                }
                return String.format("Set file to %s", fileArgument);
            }
        }

        return String.format("%s is not a valid file", fileArgument);
    }
}
