package me.chachoox.lithium.api.event.bus;

import me.chachoox.lithium.api.event.bus.api.EventBus;
import me.chachoox.lithium.api.event.bus.api.IListener;
import me.chachoox.lithium.api.event.bus.api.Subscriber;
import me.chachoox.lithium.api.interfaces.Minecraftable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class SimpleBus implements EventBus, Minecraftable {
    private final Map<Class<?>, List<IListener>> listeners;
    private final Set<Subscriber> subscribers;
    private final Set<IListener> subbedlisteners;

    public SimpleBus() {
        listeners = new ConcurrentHashMap<>();
        subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
        subbedlisteners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    @Override
    public void dispatch(Object object) {
        List<IListener> listening = listeners.get(object.getClass());
        if (listening != null) {
            for (IListener listener : listening) {
                listener.call(object);
            }
        }
    }

    @Override
    public void dispatch(Object object, Class<?> type) {
        List<IListener> listening = listeners.get(object.getClass());
        if (listening != null) {
            for (IListener listener : listening) {
                if (listener.getType() == null || listener.getType() == type) {
                    listener.call(object);
                }
            }
        }
    }

    @Override
    public void register(IListener<?> listener) {
        if (subbedlisteners.add(listener)) {
            addAtPriority(listener, listeners.computeIfAbsent(listener.getTarget(), v -> new CopyOnWriteArrayList<>()));
        }
    }

    @Override
    public void unregister(IListener listener) {
        if (subbedlisteners.remove(listener)) {
            List<IListener> list = listeners.get(listener.getTarget());
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    @Override
    public void dispatchReversed(Object object, Class<?> type) {
        List<IListener> list = listeners.get(object.getClass());
        if (list != null) {
            ListIterator<IListener> li = list.listIterator(list.size());
            while(li.hasPrevious()) {
                IListener l = li.previous();
                if (l != null && (l.getType() == null || l.getType() == type)) {
                    l.call(object);
                }
            }
        }
    }

    @Override
    public void subscribe(Object object) {
        if (object instanceof Subscriber) {
            Subscriber subscriber = (Subscriber) object;
            for (IListener<?> listener : subscriber.getListeners()) {
                register(listener);
            }
            subscribers.add(subscriber);
        }
    }

    @Override
    public void unsubscribe(Object object) {
        if (object instanceof Subscriber) {
            Subscriber subscriber = (Subscriber) object;
            for (IListener<?> listener : subscriber.getListeners()) {
                unregister(listener);
            }
            subscribers.remove(subscriber);
        }
    }

    @Override
    public boolean isSubscribed(Object object) {
        if (object instanceof Subscriber) {
            return subscribers.contains(object);
        }
        else if (object instanceof IListener) {
            return subbedlisteners.contains(object);
        }

        return false;
    }

    @Override
    public boolean hasSubscribers(Class<?> clazz) {
        List<IListener> listening = listeners.get(clazz);
        return listening != null && !listening.isEmpty();
    }

    @Override
    public boolean hasSubscribers(Class<?> clazz, Class<?> type) {
        List<IListener> listening = listeners.get(clazz);
        return listening != null && listening.stream().anyMatch(listener -> listener.getType() == null || listener.getType() == type);
    }

    private void addAtPriority(IListener<?> listener, List<IListener> list) {
        int index = 0;
        while (index < list.size() && listener.getPriority() < list.get(index).getPriority()) {
            index++;
        }
        list.add(index, listener);
    }
}