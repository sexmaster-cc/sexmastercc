package me.chachoox.lithium.impl.command.commands.misc;

import me.chachoox.lithium.impl.command.Command;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderCommand extends Command {
    public FolderCommand() {
        super(new String[]{"Folder", "OpenFolder"});
    }

    @Override
    public String execute() {
        try {
            Path lithium = Paths.get("Lithium");
            Desktop.getDesktop().open(lithium.toFile());
            return String.format("Opened %s folder", lithium.getFileName());
        } catch (IOException e) {
            return  "Could not find (Lithium) folder";
        }
    }
}
