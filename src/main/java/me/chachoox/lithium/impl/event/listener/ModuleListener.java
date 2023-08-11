package me.chachoox.lithium.impl.event.listener;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.interfaces.Minecraftable;

public abstract class ModuleListener<M, E> extends Listener<E> implements Minecraftable {
    protected final M module;

    public ModuleListener(M module, Class<? super E> target) {
        this(module, target, 10);
    }

    public ModuleListener(M module, Class<? super E> target, int priority) {
        this(module, target, priority, null);
    }

    public ModuleListener(M module, Class<? super E> target, Class<?> type) {
        this(module, target, 10, type);
    }

    public ModuleListener(M module, Class<? super E> target, int priority, Class<?> type) {
        super(target, priority, type);
        this.module = module;
    }

}

