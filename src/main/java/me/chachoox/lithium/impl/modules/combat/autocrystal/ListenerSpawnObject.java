package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketUseEntity;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ListenerSpawnObject extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSpawnObject>> {
    public ListenerSpawnObject(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
        SPacketSpawnObject packet = event.getPacket();
        if (module.boost.getValue()) {
            if (packet.getType() == 51 && module.placeSet.contains(new BlockPos(packet.getX(), packet.getY(), packet.getZ()).down())) {
                ICPacketUseEntity hitPacket = (ICPacketUseEntity) new CPacketUseEntity();
                hitPacket.setEntityId(packet.getEntityID());
                hitPacket.setAction(CPacketUseEntity.Action.ATTACK);
                PacketUtil.send((Packet<?>) hitPacket);
                module.predictedId = packet.getEntityID();
                module.attackMap.put(packet.getEntityID(), module.attackMap.containsKey(packet.getEntityID()) ? module.attackMap.get(packet.getEntityID()) + 1 : 1);
                mc.player.swingArm(module.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                module.breakTimer.reset();
            }
        }
    }
}
