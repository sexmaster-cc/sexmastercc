package me.chachoox.lithium.api.event.bus;

import me.chachoox.lithium.api.event.bus.api.EventBus;
import me.chachoox.lithium.api.event.bus.api.IListener;

public abstract class Listener<E> implements IListener<E> {
    private final Class<? super E> event;
    private final Class<?> type;
    private final int priority;

    public Listener(Class<? super E> event) {
        this(event, EventBus.DEFAULT_PRIORITY, null);
    }

    public Listener(Class<? super E> event, Class<?> type) {
        this(event, EventBus.DEFAULT_PRIORITY, type);
    }

    public Listener(Class<? super E> event, int priority) {
        this(event, priority, null);
    }

    public Listener(Class<? super E> target, int priority, Class<?> type) {
        this.priority = priority;
        this.event   = target;
        this.type     = type;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Class<? super E> getTarget() {
        return event;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}

