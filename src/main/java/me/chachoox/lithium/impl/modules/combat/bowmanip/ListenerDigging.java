package me.chachoox.lithium.impl.modules.combat.bowmanip;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

public class ListenerDigging extends ModuleListener<BowManip, PacketEvent.Send<CPacketPlayerDigging>> {

    private final static double n = 1.0E-5;

    public ListenerDigging(BowManip module) {
        super(module, PacketEvent.Send.class, CPacketPlayerDigging.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketPlayerDigging> event) {
        if (mc.player != null) {
            if (event.getPacket().getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                if (!mc.player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow && module.timer.getTime() - module.last >= (module.ticks.getValue() * 50)) {
                    module.last = System.currentTimeMillis();
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    for (int i = 0; i < (module.spoofs.getValue()); ++i) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - n, mc.player.posZ, true));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + n, mc.player.posZ, false));
                    }
                }
            }
        }
    }
}
