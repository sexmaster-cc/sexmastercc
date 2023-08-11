package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;

public class ListenerSpawnObject extends ModuleListener<FastBreak, PacketEvent.Receive<SPacketSpawnObject>> {
    public ListenerSpawnObject(FastBreak module) {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
        if (module.pos != null) {
            SPacketSpawnObject packet = event.getPacket();
            BlockPos packetPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ()).down();
            if (packet.getType() == 51 && packetPos.equals(module.pos) && module.crystalAttack) {
                module.crystalPos = packetPos;
                module.crystalID = packet.getEntityID();
                module.crystalAttack = false;
            }
        }
    }
}