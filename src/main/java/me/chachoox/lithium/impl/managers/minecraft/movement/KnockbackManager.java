package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class KnockbackManager extends SubscriberImpl implements Minecraftable {

    public static String[] KB_BOOST_ALIAS = new String[]{"KbBoost", "KnockBackBoost", "ExplosionBoost", "kbb", "kb", "eb", "explode"};
    public static String[] BOOST_REDUCTION_ALIAS = new String[]{"BoostReduction", "boostSpeed", "explosionboost", "motion", "explodespeed"};

    public static String KB_BOOST_DESCRIPTION = "Multiplies the speed if we are in air and a crystal has exploded near us.";
    public static String BOOST_REDUCTION_DESCRIPTION = "How much we want to multiply the speed by.";

    protected StopWatch explosionTimer = new StopWatch();
    protected SPacketExplosion getExplosion;
    protected boolean caughtExplosion;

    protected StopWatch velocityTimer = new StopWatch();
    protected SPacketEntityVelocity getVelocity;
    protected boolean caughtAttack;

    public KnockbackManager() {
        this.listeners.add(new Listener<UpdateEvent>(UpdateEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(UpdateEvent event) {
                if (explosionTimer.passed(400)) {
                    caughtExplosion = false;
                }

                if (velocityTimer.passed(250)) {
                    caughtAttack = false;
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketEntityVelocity>>(PacketEvent.Receive.class, SPacketEntityVelocity.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketEntityVelocity> event) {
                getVelocity = event.getPacket();
                velocityTimer.reset();
                caughtAttack = true;
            }
        });
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketExplosion>>(PacketEvent.Receive.class, SPacketExplosion.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketExplosion> event) {
                getExplosion = event.getPacket();
                explosionTimer.reset();
                caughtExplosion = true;
            }
        });
    }

    public boolean shouldBoost(boolean doKbBoost) {
        return doKbBoost && (caughtExplosion && getExplosion.getStrength() == 6.0 || caughtAttack && (getVelocity.getMotionX() >= 6 || getVelocity.getMotionZ() >= 6)) && !mc.player.onGround;
    }
}
