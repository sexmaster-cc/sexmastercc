package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fakeplayer.FakePlayer;

public class FakePlayerCommand extends Command {
    public FakePlayerCommand() {
        super(new String[]{"FakePlayer", "Fk", "polloshelper"}, new Argument("clear"));
    }

    @Override
    public String execute() {
        final FakePlayer FAKE_PLAYER = Managers.MODULE.get(FakePlayer.class);

        if (this.getArgument("clear").isPresent()) {
            if (FAKE_PLAYER.getPlayerPositions().isEmpty()) {
                return "There are no fakeplayer positions";
            }
            FAKE_PLAYER.getPlayerPositions().clear();
            return "Cleared fakeplayer positions";
        }

        FAKE_PLAYER.toggle();
        return "Toggling fakeplayer module";
    }
}
