package me.chachoox.lithium.impl.command.commands.helper;

import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.quiver.Quiver;

public class QuiverCommand extends Command {
    public QuiverCommand() {
        super(new String[]{"Quiv", "Qv"}, new Argument("add/del/list/clear"), new Argument("arrow"));
    }

    @Override
    public String execute() {
        final Quiver QUIVER = Managers.MODULE.get(Quiver.class);

        final String arg = this.getArgument("add/del/list/clear").getValue();

        if (arg.equalsIgnoreCase("LIST")) {
            if (QUIVER.getList().isEmpty()) {
                return "There is no items added";
            }
            return QUIVER.getList().toString();
        }

        if (arg.equalsIgnoreCase("CLEAR")) {
            if (QUIVER.getList().isEmpty()) {
                return "There is no items added";
            }
            QUIVER.getList().clear();
            return "Cleared Quiver list";
        }

        final String arrow = this.getArgument("arrow").getLabel();
        switch (arg.toUpperCase()) {
            case ("ADD"):
                if (QUIVER.getList().contains(arrow)) {
                    return String.format("[%s] is already in the list", arrow);
                }
                QUIVER.getList().add(TextUtil.formatString(arrow));
                return "Added " + TextColor.AQUA + TextUtil.getFixedName(arrow) + TextColor.LIGHT_PURPLE + " to the list";
            case ("DEL"):
            case ("DELETE"):
            case ("REMOVE"):
                QUIVER.getList().remove(TextUtil.formatString(arrow));
                return "Removed " + TextColor.RED + TextUtil.getFixedName(arrow) + TextColor.LIGHT_PURPLE + " from the list";
        }

        return getSyntax();
    }
}
