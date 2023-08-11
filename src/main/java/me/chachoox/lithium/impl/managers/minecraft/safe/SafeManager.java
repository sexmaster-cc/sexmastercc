package me.chachoox.lithium.impl.managers.minecraft.safe;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.misc.GameLoopEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SafeManager extends SubscriberImpl implements Minecraftable {
    private final AtomicBoolean safe = new AtomicBoolean(false);
    private final StopWatch timer = new StopWatch();

    public SafeManager() {
        this.listeners.add(new Listener<GameLoopEvent>(GameLoopEvent.class) {
            @Override
            public void call(GameLoopEvent event) {
                if (timer.passed(25L)) {
                    runThread();
                    timer.reset();
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketSpawnObject>>(PacketEvent.Receive.class, SafeManager.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketSpawnObject> event) {
                SPacketSpawnObject packet = event.getPacket();
                if (packet.getType() == 51 && mc.player != null) {
                    if (DamageUtil.calculate(new BlockPos(packet.getX(), packet.getY(), packet.getZ()).down()) > 4.0F) {
                        setSafe(false);
                    }
                }
            }
        });
    }

    public boolean isSafe() {
        return safe.get();
    }

    public void setSafe(boolean safe) {
        this.safe.set(safe);
    }

    protected void runThread() {
        if (mc.player != null && mc.world != null) {
            List<Entity> crystals = new ArrayList<>(mc.world.loadedEntityList);
            SafetyRunnable runnable = new SafetyRunnable(this, crystals);
            Managers.THREAD.submit(runnable);
        }
    }

}
