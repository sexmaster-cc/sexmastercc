package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Command;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CoordsCommand extends Command {
    public CoordsCommand() {
        super(new String[]{"Coords", "Coord", "WhereTfAmIAt"});
    }

    @Override
    public String execute() {
        String coordinates =
                "X: "
                + (int) mc.player.posX
                + ", Y: "
                + (int) mc.player.posY
                + ", Z: "
                + (int) mc.player.posZ;
        StringSelection stringSelection = new StringSelection(coordinates);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        return "Copied current position to clipboard";
    }

}
