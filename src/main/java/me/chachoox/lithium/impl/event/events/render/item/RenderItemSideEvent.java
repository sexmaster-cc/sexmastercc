package me.chachoox.lithium.impl.event.events.render.item;

import me.chachoox.lithium.api.event.events.Event;

public class RenderItemSideEvent extends Event {
    float x = 1.0f;
    float y = 1.0f;
    float z = 1.0f;

    public RenderItemSideEvent() {
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}