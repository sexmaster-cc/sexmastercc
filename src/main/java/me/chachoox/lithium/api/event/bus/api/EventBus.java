package me.chachoox.lithium.api.event.bus.api;

public interface EventBus {
    int DEFAULT_PRIORITY = 10;

    void dispatch(Object object);

    void dispatch(Object object, Class<?> type);

    void register(IListener<?> listener);

    void unregister(IListener<?> listener);

    void dispatchReversed(Object object, Class<?> type);

    void subscribe(Object object);

    void unsubscribe(Object object);

    boolean isSubscribed(Object object);

    boolean hasSubscribers(Class<?> clazz);

    boolean hasSubscribers(Class<?> clazz, Class<?> type);
}