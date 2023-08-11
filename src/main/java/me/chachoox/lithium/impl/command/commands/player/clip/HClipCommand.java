package me.chachoox.lithium.impl.command.commands.player.clip;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.entity.Entity;

public class HClipCommand extends Command {
    public HClipCommand() {
        super(new String[]{"HClip", "hc"}, new Argument("blocks"));
    }

    @Override
    public String execute() {
        double h = Double.parseDouble(this.getArgument("blocks").getValue());
        Entity entity = mc.player.getRidingEntity() != null ? mc.player.getRidingEntity() : mc.player;
        entity.setPosition(entity.posX + h * Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0f)), entity.posY, entity.posZ + h * Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0f)));
        return "HClipped you " + h + " blocks";
    }
}