package me.chachoox.lithium.impl.event.events.render.main;

public class Render3DEvent {
    private final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

}
