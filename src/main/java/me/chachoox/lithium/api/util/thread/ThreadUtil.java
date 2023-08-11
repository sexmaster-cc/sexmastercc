package me.chachoox.lithium.api.util.thread;

import me.chachoox.lithium.api.interfaces.Minecraftable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class ThreadUtil implements Minecraftable {

    public static final ThreadFactory FACTORY = newDaemonThreadFactoryBuilder().setNameFormat("Lithium-Thread-%d").build();

    public static ScheduledExecutorService newDaemonScheduledExecutor(String name) {
        ThreadFactoryBuilder factory = newDaemonThreadFactoryBuilder();
        factory.setNameFormat("Lithium-" + name + "-%d");
        return Executors.newSingleThreadScheduledExecutor(factory.build());
    }

    public static ExecutorService newDaemonCachedThreadPool() {
        return Executors.newCachedThreadPool(FACTORY);
    }

    public static ExecutorService newFixedThreadPool(int size) {
        ThreadFactoryBuilder factory = newDaemonThreadFactoryBuilder();
        factory.setNameFormat("Lithium-Fixed-%d");
        return Executors.newFixedThreadPool(Math.max(size, 1), factory.build());
    }

    public static ThreadFactoryBuilder newDaemonThreadFactoryBuilder() {
        ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setDaemon(true);
        return factory;
    }
}
