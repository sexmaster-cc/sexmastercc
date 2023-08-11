package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerTick extends ModuleListener<BowManip, TickEvent> {
    public ListenerTick(BowManip module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > module.fire.getValue() && module.fire.getValue() > 3.0F) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.player.stopActiveHand();
        }
    }
}
