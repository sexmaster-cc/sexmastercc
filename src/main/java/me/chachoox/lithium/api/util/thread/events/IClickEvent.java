package me.chachoox.lithium.api.util.thread.events;

public interface IClickEvent {
    void setRunnable(Runnable runnable);

    Runnable getRunnable();
}
