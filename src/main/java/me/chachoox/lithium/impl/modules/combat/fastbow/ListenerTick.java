package me.chachoox.lithium.impl.modules.combat.fastbow;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerTick extends ModuleListener<FastBow, TickEvent> {
    public ListenerTick(FastBow module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null && holdingBow() && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > module.ticks.getValue()) {
            PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            mc.player.stopActiveHand();
        }
    }

    private boolean holdingBow() {
        return mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBow;
    }
}
