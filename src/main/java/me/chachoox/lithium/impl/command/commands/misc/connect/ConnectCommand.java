package me.chachoox.lithium.impl.command.commands.misc.connect;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class ConnectCommand extends Command {
    public ConnectCommand() {
        super(new String[]{"Connect", "ConnectTo", "join", "con"}, new Argument("ip"));
    }

    @Override
    public String execute() {
        final ServerData serverData = new ServerData("", this.getArgument("ip").getValue(), false);
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, serverData));
        return "Connecting...";
    }
}
