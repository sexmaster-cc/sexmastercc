package me.chachoox.lithium.impl.modules.misc.pingspoof;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.thread.ThreadUtil;
import me.chachoox.lithium.impl.event.events.misc.ShutDownEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.network.Packet;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingSpoof extends Module {

    private final NumberProperty<Integer> latency =
            new NumberProperty<>(
                    50, 0, 3000,
                    new String[]{"Latency", "Delay", "ping", "spoof", "spoofer", "Spoofamount", "p", "SHOOTPOLLOSXD"},
                    "How much we want to add to our actual latency."
            );

    private final NumberProperty<Integer> jitter =
            new NumberProperty<>(
                    10, 0, 50,
                    new String[]{"Jitter", "jit", "jitte", "j", "IFUCKINGHATEPOLLOSXD"},
                    "Selects a random number that isnt higher than this value and adds it to the ping."
            );

    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService service;
    private final Random rand = new Random();

    public PingSpoof() {
        super("PingSpoof", new String[]{"PingSpoof", "PingDelay", "BeanerNet"}, "Spoofs your in-game ping", Category.MISC);
        this.offerProperties(latency, jitter);
        this.offerListeners(new ListenerKeepAlive(this), new LambdaListener<>(DisconnectEvent.class, event -> packets.clear()));
        service = ThreadUtil.newDaemonScheduledExecutor("PingSpoof");
        Bus.EVENT_BUS.register(new Listener<ShutDownEvent>(ShutDownEvent.class) {
            @Override
            public void call(ShutDownEvent event) {
                service.shutdown();
            }
        });
    }

    @Override
    public void onDisable() {
        if (!packets.isEmpty()) {
            packets.forEach(packet -> {
                if (packet != null) {
                    mc.player.connection.sendPacket(packet);
                }
            });
            packets.clear();
        }
    }

    protected void onPacket(Packet<?> packet) {
        packets.add(packet);
        service.schedule(() -> {
            if (mc.player != null) {
                Packet<?> p = packets.poll();
                if (p != null) {
                    PacketUtil.sendPacketNoEvent(p);
                }
            }
        }, latency.getValue() + getJitter(), TimeUnit.MILLISECONDS);
    }

    public int getJitter() {
        int jitter = 0;
        if (this.jitter.getValue() != 0) {
            jitter = rand.nextInt(this.jitter.getValue());
        }
        return jitter;
    }

    public int getLatency() {
        return latency.getValue();
    }
}
