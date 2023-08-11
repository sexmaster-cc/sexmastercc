package me.chachoox.lithium.api.observable;

import java.util.LinkedList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<? super T>> observers = new LinkedList<>();

    public T onChange(T value) {
        for (Observer<? super T> observer : observers) {
            observer.onChange(value);
        }

        return value;
    }

    public void addObserver(Observer<? super T> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer<? super T> observer) {
        observers.remove(observer);
    }

}
