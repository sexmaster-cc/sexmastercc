package me.chachoox.lithium.api.util.thread.events;

import net.minecraft.util.text.event.ClickEvent;

public class RunnableClickEvent extends ClickEvent {
    public RunnableClickEvent(Runnable runnable) {
        super(Action.RUN_COMMAND, "$runnable$");
        ((IClickEvent) this).setRunnable(runnable);
    }
}