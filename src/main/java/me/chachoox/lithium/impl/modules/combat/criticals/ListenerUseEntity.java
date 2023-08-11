package me.chachoox.lithium.impl.modules.combat.criticals;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class ListenerUseEntity extends ModuleListener<Criticals, PacketEvent.Send<CPacketUseEntity>> {
    public ListenerUseEntity(Criticals module) {
        super(module, PacketEvent.Send.class, CPacketUseEntity.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketUseEntity> event) {
        final CPacketUseEntity packet = event.getPacket();
        if (mc.player.onGround && packet.getAction() == CPacketUseEntity.Action.ATTACK) {
            final Entity entity = packet.getEntityFromWorld(mc.world);
            if (entity == null) {
                return;
            }

            if (module.yDifference.getValue() && ((int) entity.posY) >= mc.player.posY) {
                //Logger.getLogger().log(TextColor.RED + "you cant crit dat nigga", false);
                return;
            }

            if (entity instanceof EntityLivingBase && !(mc.player.isInWater() || mc.player.isInLava())) {
                switch (module.mode.getValue()) {
                    case PACKET: {
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.05F, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.03F, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        break;
                    }
                    case BYPASS: { //TODO: PUT FUTURE NCPSTRICT HERE THIS ONE IS ASS
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.062600301692775, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.07260029960661, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                        break;
                    }
                }
            } else if (entity instanceof EntityBoat) {
                for (int i = 0; i < 15; ++i) {
                    PacketUtil.sendPacketNoEvent(new CPacketUseEntity(entity));
                }
            }
        }
    }
}
