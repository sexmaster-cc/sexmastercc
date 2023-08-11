package me.chachoox.lithium.impl.managers.minecraft.combat;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.popcounter.PopCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;
import java.util.Map;

public class PopManager extends SubscriberImpl implements Minecraftable {

    private final Map<String, Integer> popMap = new HashMap<>();

    public PopManager() {
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketEntityStatus>>
                (PacketEvent.Receive.class, Integer.MIN_VALUE, SPacketEntityStatus.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketEntityStatus> event) {
                if (mc.player == null || mc.world == null) {
                    return;
                }

                final SPacketEntityStatus packet = event.getPacket();
                if (packet.getOpCode() == 35) {
                    Entity entity = packet.getEntity(mc.world);
                    String name = entity.getName();
                    if (entity instanceof EntityPlayer) {
                        boolean contains = popMap.containsKey(name);
                        popMap.put(name, contains ? popMap.get(name) + 1 : 1);

                        TotemPopEvent totemPopEvent = new TotemPopEvent((EntityPlayer) entity);
                        Bus.EVENT_BUS.dispatch(totemPopEvent);
                    }
                }
            }
        });
        this.listeners.add(new Listener<DeathEvent>(DeathEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(DeathEvent event) {
                final EntityLivingBase player = event.getEntity();
                if (player instanceof EntityPlayer) {
                    String name = player.getName();
                    if (popMap.containsKey(name)) {
                        popMap.remove(name, popMap.get(name));
                    }
                }
            }
        });
        this.listeners.add(new Listener<ConnectionEvent.Leave>(ConnectionEvent.Leave.class, Integer.MIN_VALUE) {
            @Override
            public void call(ConnectionEvent.Leave event) {
                String name = event.getName();
                if (name == null) {
                    return;
                }

                final PopCounter POP_COUNTER = Managers.MODULE.get(PopCounter.class);
                if (popMap.containsKey(name) && POP_COUNTER.clearOnLog()) {
                    popMap.remove(name, popMap.get(name));
                }
            }
        });
        this.listeners.add(new Listener<EntityWorldEvent.Remove>(EntityWorldEvent.Remove.class, Integer.MIN_VALUE) {
            @Override
            public void call(EntityWorldEvent.Remove event) {
                Entity player = event.getEntity();
                if (player == null) {
                    return;
                }

                if (player instanceof EntityPlayer) {
                    String name = player.getName();
                    boolean isSelf = player == mc.player;
                    final PopCounter POP_COUNTER = Managers.MODULE.get(PopCounter.class);
                    if (popMap.containsKey(name) && !isSelf && POP_COUNTER.clearOnVisualRange()) {
                        popMap.remove(name, popMap.get(name));
                    }
                }
            }
        });
        this.listeners.add(new Listener<WorldClientEvent.Load>(WorldClientEvent.Load.class, Integer.MIN_VALUE) {
            @Override
            public void call(WorldClientEvent.Load event) {
                popMap.clear();
            }
        });
        this.listeners.add(new Listener<DisconnectEvent>(DisconnectEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(DisconnectEvent event) {
                popMap.clear();
            }
        });
    }

    public final Map<String, Integer> getPopMap() {
        return popMap;
    }
}
