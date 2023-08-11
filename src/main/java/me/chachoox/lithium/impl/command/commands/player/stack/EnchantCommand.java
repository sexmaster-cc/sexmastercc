package me.chachoox.lithium.impl.command.commands.player.stack;

import me.chachoox.lithium.impl.command.Argument;
import me.chachoox.lithium.impl.command.Command;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.util.text.translation.I18n;

import java.util.Objects;

public class EnchantCommand extends Command {
    public EnchantCommand() {
        super(new String[]{"Enchant", "en"}, new Argument("level"), new Argument("enchantment"));
    }

    @Override
    public String execute() {
        short level = (short) Integer.parseInt(this.getArgument("level").getValue());

        ItemStack stack = mc.player.inventory.getCurrentItem();
        if (stack.isEmpty()) {
            return "Hold an item to use this command";
        }

        if (this.getArgument("enchanment").isPresent()) {
            Enchantment enchantment = getEnchantment(this.getArgument("enchantment").getValue());
            if (enchantment == null) {
                return "Could find Enchantment " + this.getArgument("enchantment");
            }

            stack.addEnchantment(enchantment, level);
            return setStack(stack);
        }

        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (!enchantment.isCurse()) {
                stack.addEnchantment(enchantment, level);
            }
        }

        return setStack(stack);
    }

    private String setStack(ItemStack stack) {
        int slot = mc.player.inventory.currentItem + 36;
        if (mc.player.isCreative()) {
            mc.player.connection.sendPacket(new CPacketCreativeInventoryAction(slot, stack));
        } else if (mc.isSingleplayer()) {
            EntityPlayerMP player = Objects.requireNonNull(mc.getIntegratedServer()).getPlayerList().getPlayerByUUID(mc.player.getUniqueID());
            //noinspection ConstantConditions
            if (player != null) {
                player.inventoryContainer.putStackInSlot(slot, stack);
                return "Item enchanted";
            }
        } else {
            return "Not Creative or singleplayer, Enchantments are client sided";
        }

        return "hello young thug";
    }


    @SuppressWarnings("deprecation")
    public static Enchantment getEnchantment(String prefixIn) {
        String prefix = prefixIn.toLowerCase();
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            String s = I18n.translateToLocal(enchantment.getName());
            if (s.toLowerCase().startsWith(prefix)) {
                return enchantment;
            }
        }
        return null;
    }
}