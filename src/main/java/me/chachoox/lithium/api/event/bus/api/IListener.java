package me.chachoox.lithium.api.event.bus.api;

public interface IListener<E> extends Caller<E> {
    int getPriority();

    Class<? super E> getTarget();

    Class<?> getType();
}
