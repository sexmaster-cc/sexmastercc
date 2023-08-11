package me.chachoox.lithium.impl.command.commands.player.clip;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.entity.Entity;

public class VClipCommand extends Command {
    public VClipCommand() {
        super(new String[]{"VClip", "vc"}, new Argument("blocks"));
    }

    @Override
    public String execute() {
        double amount = Double.parseDouble(this.getArgument("blocks").getValue());
        Entity entity = mc.player.getRidingEntity() != null ? mc.player.getRidingEntity() : mc.player;
        entity.setPosition(entity.posX, entity.posY + amount, entity.posZ);
        return "VClipped you " + amount + " blocks";
    }
}
