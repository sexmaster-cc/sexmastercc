package me.chachoox.lithium.api.event.events;

public class Event {

    private boolean canceled = false;

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

