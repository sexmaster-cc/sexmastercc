package me.chachoox.lithium.api.util.thread;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SafeFinishable extends Finishable implements SafeRunnable {
    public SafeFinishable() {
        this(new AtomicBoolean());
    }

    public SafeFinishable(AtomicBoolean finished) {
        super(finished);
    }

    @Override
    public void run() {
        try {
            runSafely();
        } catch (Throwable t) {
            handle(t);
        } finally {
            setFinished(true);
        }
    }

    @Deprecated
    protected void execute() {
        // NOOP
    }

}
