package me.chachoox.lithium.impl.command.commands.misc.connect;

import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.command.Command;

public class DisconnectCommand extends Command {
    public DisconnectCommand() {
        super(new String[]{"Disconnect", "Leave", "Quit"});
    }

    @Override
    public String execute() {
        if (mc.isSingleplayer()) {
            return "You are in single player";
        }

        if (mc.getConnection() == null) {
            mc.world.sendQuittingDisconnectingPacket();
        } else {
            String disconnectMessage = "Left server due to disconnect command being executed";
            NetworkUtil.disconnect(disconnectMessage);
        }

        return "Disconnecting...";
    }
}
