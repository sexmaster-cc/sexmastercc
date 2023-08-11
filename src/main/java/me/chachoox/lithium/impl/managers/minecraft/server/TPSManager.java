package me.chachoox.lithium.impl.managers.minecraft.server;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import java.util.ArrayDeque;

public class TPSManager extends SubscriberImpl implements Minecraftable {

    private final ArrayDeque<Float> queue = new ArrayDeque<>(20);
    private float currentTps;
    private long time;
    private float tps;

    public TPSManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketTimeUpdate>>(PacketEvent.Receive.class, SPacketTimeUpdate.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketTimeUpdate> event) {
                if (time != 0) {
                    if (queue.size() > 20) {
                        queue.poll();
                    }

                    currentTps = Math.max(0.0f, Math.min(20.0f, 20.0f * (1000.0f / (System.currentTimeMillis() - time))));
                    queue.add(currentTps);
                    float factor = 0.0f;
                    for (Float qTime : queue) {
                        factor += Math.max(0.0f, Math.min(20.0f, qTime));
                    }

                    if (queue.size() > 0) {
                        factor /= queue.size();
                    }
                    tps = factor;
                }

                time = System.currentTimeMillis();
            }
        });
    }

    public float getCurrentTps() {
        return currentTps;
    }

    public float getTps() {
        return tps;
    }

    public float getFactor() {
        return tps / 20.0f;
    }
}
