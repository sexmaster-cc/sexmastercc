package me.chachoox.lithium.impl.modules.movement.packetfly.util;

public class TeleportIDs {
    private double x;
    private double y;
    private double z;
    private long time;

    public TeleportIDs(double x, double y, double z, long time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public long getTime() {
        return this.time;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setTime(long time) {
        this.time = time;
    }

}

