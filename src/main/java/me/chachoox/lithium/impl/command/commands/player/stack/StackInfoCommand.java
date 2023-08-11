package me.chachoox.lithium.impl.command.commands.player.stack;

import me.chachoox.lithium.impl.command.Command;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackInfoCommand extends Command {
    public StackInfoCommand() {
        super(new String[]{"StackInfo", "SI", "DumpStack", "DS"});
    }

    @Override
    public String execute() {
        ItemStack stack = mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem);
        String info;
        info = stack.getDisplayName();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        stack.writeToNBT(nbtTagCompound);
        info = info + ": " + nbtTagCompound;
        return info;
    }
}
