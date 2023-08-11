package me.chachoox.lithium.impl.managers.client;

import me.chachoox.lithium.api.util.thread.GlobalExecutor;
import me.chachoox.lithium.api.util.thread.SafeRunnable;

public class ThreadManager implements GlobalExecutor {

    public void submit(SafeRunnable runnable) {
        submitRunnable(runnable);
    }

    public void submitRunnable(Runnable runnable) {
        EXECUTOR.submit(runnable);
    }

    public void shutDown() {
        EXECUTOR.shutdown();
    }

}
