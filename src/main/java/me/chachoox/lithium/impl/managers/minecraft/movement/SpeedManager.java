package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import net.minecraft.util.math.Vec3d;

public class SpeedManager extends SubscriberImpl implements Minecraftable {

    private final StopWatch timer = new StopWatch();
    private Vec3d last = new Vec3d(0, 0, 0);
    private double speed = 0.0f;

    public SpeedManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class) {
            @Override
            public void call(TickEvent event) {
                if (event.isSafe() && timer.passed(40)) {
                    speed = MathUtil.distance2D(mc.player.getPositionVector(), last);
                    last = mc.player.getPositionVector();
                    timer.reset();
                }
            }
        });
    }

    public double getSpeed() {
        return getSpeedBpS() * 3.6;
    }

    public double getSpeedBpS() {
        return speed * 20;
    }

}
