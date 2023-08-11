package me.chachoox.lithium.impl.command.commands.misc.search;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SearchCommand extends Command {
    public SearchCommand() {
        super(new String[]{"Search", "Searchington"}, new Argument("namemc/crafty/laby"), new Argument("player"));
    }

    @Override
    public String execute() {
        final String arg = this.getArgument("namemc/crafty/laby").getValue();
        final String player = this.getArgument("player").getValue();

        if (arg.equalsIgnoreCase("NameMC")) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://namemc.com/search?q=" + player));
                    return "Opening " + player + " on site -> (NameMC.com)";
                } catch (IOException | URISyntaxException e) {
                    return "Unknown error while trying to open site -> (NameMC.com)";
                }
            }
        }

        if (arg.equalsIgnoreCase("Crafty")) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://crafty.gg/players/" + player));
                    return "Opening " + player + " on site -> (Crafty.gg)";
                } catch (IOException | URISyntaxException e) {
                    return "Unknown error while trying to open site -> (Crafty.gg)";
                }
            }
        }

        if (arg.equalsIgnoreCase("Laby")) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://laby.net/@" + player));
                    return "Opening " + player + " on site -> (Laby.net)";
                } catch (IOException | URISyntaxException e) {
                    return "Unknown error while trying to open site -> (Laby.net)";
                }
            }
        }

        return this.getSyntax();
    }
}
