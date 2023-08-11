package me.chachoox.lithium.impl.modules.render.pearltrace.util;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class ThrownEntity {

    private long time;
    private final ArrayList<Vec3d> vertices;

    public ThrownEntity(long time, ArrayList<Vec3d> vertices) {
        this.time = time;
        this.vertices = vertices;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Vec3d> getVertices() {
        return vertices;
    }

}