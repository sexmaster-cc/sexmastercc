package me.chachoox.lithium.api.util.math;

import net.minecraft.util.math.Vec3i;

public class Sphere {
    private static final Vec3i[] SPHERE = new Vec3i[4187707];
    private static final int[] INDICES = new int[101];

    static {
        SPHERE[SPHERE.length - 1] = new Vec3i(Integer.MAX_VALUE, 0, 0);
    }

    private Sphere() {
        throw new AssertionError();
    }

    public static int getRadius(double radius) {
        return INDICES[MathUtil.clamp((int) Math.ceil(radius), 0, INDICES.length)];
    }

    public static Vec3i get(int index) {
        return SPHERE[index];
    }

    public static int getLength() {
        return SPHERE.length;
    }

}
