package me.chachoox.lithium.api.observable;

public interface Observer<T> {

    void onChange(T value);

}
