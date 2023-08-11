package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.blocks.DamageBlockEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;

public class ListenerDamage extends ModuleListener<FastBreak, DamageBlockEvent> {
    public ListenerDamage(FastBreak module) {
        super(module, DamageBlockEvent.class);
    }

    @Override
    public void call(DamageBlockEvent event) {
        if (MineUtil.canBreak(event.getPos())
                && !mc.player.capabilities.isCreativeMode
                && module.timer.passed(250L)
                && module.isBlockValid(mc.world.getBlockState(event.getPos()).getBlock())) {
            switch (module.mode.getValue()) {
                case PACKET: {
                    boolean aborted = false;
                    if (module.pos != null && !module.pos.equals(event.getPos())) {
                        module.abortCurrentPos();
                        aborted = true;
                    }

                    if (!aborted && module.pos != null && module.pos.equals(event.getPos()) && module.auto.getValue()) {
                        module.abortCurrentPos();
                        module.timer.reset();
                        return;
                    }

                    if (module.pos == null || module.auto.getValue()) {
                        setPos(event);
                    }

                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing());

                    PacketUtil.send(packet);

                    if (module.debug.getValue()) {
                        Logger.getLogger().log(TextColor.AQUA + "Sending normal dig packet", false);
                    }

                    event.setCanceled(true);
                    module.timer.reset();
                }
                break;
                case INSTANT: {
                    boolean aborted = false;
                    if (module.pos != null && !module.pos.equals(event.getPos())) {
                        module.abortCurrentPos();
                        aborted = true;
                    }

                    if (!aborted && module.pos != null && module.pos.equals(event.getPos()) && module.auto.getValue()) {
                        module.abortCurrentPos();
                        module.timer.reset();
                        return;
                    }

                    if (module.pos == null || module.auto.getValue()) {
                        setPos(event);
                    }

                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    CPacketPlayerDigging packet = new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing());

                    PacketUtil.send(packet);

                    if (module.debug.getValue()) {
                        Logger.getLogger().log(TextColor.AQUA + "Sending instant dig packet", false);
                    }

                    module.shouldAbort = true;

                    event.setCanceled(true);
                    module.timer.reset();

                    break;
                }
                default:
            }
        }
    }

    private void setPos(DamageBlockEvent event) {
        module.reset();
        module.pos = event.getPos();
        module.direction = event.getFacing();
    }
}
