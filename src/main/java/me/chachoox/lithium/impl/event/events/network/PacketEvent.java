package me.chachoox.lithium.impl.event.events.network;

import me.chachoox.lithium.api.event.events.Event;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.thread.SafeRunnable;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.util.ArrayDeque;
import java.util.Deque;

public class PacketEvent<T extends Packet<? extends INetHandler>> extends Event implements Minecraftable {
    private final T packet;

    private PacketEvent(T packet) {
        this.packet = packet;
    }

    public T getPacket() {
        return packet;
    }


    public static class Send<T extends Packet<? extends INetHandler>> extends PacketEvent<T> {
        public Send(T packet) {
            super(packet);
        }
    }

    public static class NoEvent<T extends Packet<? extends INetHandler>> extends PacketEvent<T> {
        public NoEvent(T packet) {
            super(packet);
        }
    }

    public static class Receive<T extends Packet<? extends INetHandler>> extends PacketEvent<T> {
        private final Deque<Runnable> postEvents = new ArrayDeque<>();

        public Receive(T packet) {
            super(packet);
        }

        public void addPostEvent(SafeRunnable runnable) {
            postEvents.add(runnable);
        }

        public Deque<Runnable> getPostEvents() {
            return postEvents;
        }
    }

    public static class Post<T extends Packet<? extends INetHandler>> extends PacketEvent<T> {
        public Post(T packet) {
            super(packet);
        }
    }

}
