package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ActionManager extends SubscriberImpl {

    private volatile boolean sneaking;
    private volatile boolean sprinting;

    public ActionManager() {
        this.listeners.add(new Listener<PacketEvent.Post<CPacketEntityAction>>(PacketEvent.Post.class, CPacketEntityAction.class) {
            @Override
            public void call(PacketEvent.Post<CPacketEntityAction> event) {
                switch (event.getPacket().getAction()) {
                    case START_SPRINTING:
                        sprinting = true;
                        break;
                    case STOP_SPRINTING:
                        sprinting = false;
                        break;
                    case START_SNEAKING:
                        sneaking = true;
                        break;
                    case STOP_SNEAKING:
                        sneaking = false;
                        break;
                    default:
                }
            }
        });
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }

}
