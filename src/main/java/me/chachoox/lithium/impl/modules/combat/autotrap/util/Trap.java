package me.chachoox.lithium.impl.modules.combat.autotrap.util;

import net.minecraft.util.math.Vec3i;

public class Trap {

    public static final Vec3i[] OFFSETS = new Vec3i[]
            {
                    new Vec3i(1, 0, 0),
                    new Vec3i(0, 0, 1),
                    new Vec3i(-1, 0, 0),
                    new Vec3i(0, 0, -1)
            };

    public static final Vec3i[] NO_STEP = new Vec3i[]
            {
                    new Vec3i(1, 1, 0),
                    new Vec3i(0, 1, 1),
                    new Vec3i(-1, 1, 0),
                    new Vec3i(0, 1, -1)
            };

    public static final Vec3i[] TOP = new Vec3i[]
            {
                    new Vec3i(0, 1, 0)
            };

    public static final Vec3i[] NO_SCAFFOLD = new Vec3i[]
            {
                    new Vec3i(0, 2, 0)
            };
}
