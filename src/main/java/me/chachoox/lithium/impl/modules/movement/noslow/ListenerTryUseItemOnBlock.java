package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;

public class ListenerTryUseItemOnBlock extends ModuleListener<NoSlow, PacketEvent.Post<CPacketPlayerTryUseItemOnBlock>> {

    public ListenerTryUseItemOnBlock(NoSlow module) {
        super(module, PacketEvent.Post.class, CPacketPlayerTryUseItemOnBlock.class);
    }

    @Override
    public void call(PacketEvent.Post<CPacketPlayerTryUseItemOnBlock> event) {
        Item item = mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
        if (module.strict.getValue() && (item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
    }

}