package me.chachoox.lithium.impl.command.commands.misc.chat;

import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.deathcoordslog.DeathCoordsLog;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class DeathCoordsCommand extends Command {
    public DeathCoordsCommand() {
        super(new String[]{"DeathCoords", "DeathSpot", "DeathLocation", "DeathPos", "WhereTfIDied"});
    }

    @Override
    public String execute() {
        DeathCoordsLog deathCoordsLog = Managers.MODULE.get(DeathCoordsLog.class);
        if (deathCoordsLog.hasDied()) {
            StringSelection stringSelection = new StringSelection(deathCoordsLog.getDeathPosition());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            return "Copied Death coordinates";
        }

        return "You haven't died yet";
    }
}
