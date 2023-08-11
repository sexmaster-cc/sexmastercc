package me.chachoox.lithium.impl.modules.player.positionspoof;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerMotion extends ModuleListener<PositionSpoof, MotionUpdateEvent> {
    public ListenerMotion(PositionSpoof module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        switch (event.getStage()) {
            case PRE: {
                PacketUtil.sendPacketNoEvent(new CPacketPlayer.Position(module.posX, module.posY, module.posZ, true));
                break;
            }
            case POST: {
                PacketUtil.sendPacketNoEvent(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                break;
            }
        }
    }
}
