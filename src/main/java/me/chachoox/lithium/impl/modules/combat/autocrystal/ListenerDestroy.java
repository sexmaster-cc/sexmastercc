package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.util.math.BlockPos;

public class ListenerDestroy extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketDestroyEntities>> {
    public ListenerDestroy(AutoCrystal module) {
        super(module, PacketEvent.Receive.class, SPacketDestroyEntities.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketDestroyEntities> event) {
        SPacketDestroyEntities packet = event.getPacket();
        for (int id : packet.getEntityIDs()) {
            Entity crystal = mc.world.getEntityByID(id);
            if (crystal instanceof EntityEnderCrystal) {
                module.placeSet.remove(new BlockPos(crystal.getPositionVector()).down());
            }
        }
    }
}
