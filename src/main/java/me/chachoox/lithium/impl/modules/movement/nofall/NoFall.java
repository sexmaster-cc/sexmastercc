package me.chachoox.lithium.impl.modules.movement.nofall;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayer;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.modules.movement.nofall.util.NoFallMode;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * @author moneymaker552
 */
public class NoFall extends Module {

    protected final EnumProperty<NoFallMode> mode =
            new EnumProperty<>(
                    NoFallMode.BUCKET,
                    new String[]{"Mode", "type", "method"},
                    "Packet: - Sends a fake position packet " +
                            "/ Bucket: - If we have a bucket we will switch to it and place it. " +
                            "/ Anti: - Lags us back whenever we are gonna hit the ground."
            );

    protected final NumberProperty<Float> distance =
            new NumberProperty<>(
                    5.0f, 3.0f, 10.0f, 0.1f,
                    new String[]{"Distance", "dist", "falldistance"},
                    "How much our fall distance has to be to attempt to avoid fall damage"
            );

    protected StopWatch stopWatch = new StopWatch();

    public NoFall() {
        super("NoFall", new String[]{"NoFall", "NoFallDamage", "AntiFallDamage"}, "Multiple methods of avoiding dying of fall damage", Category.MOVEMENT);
        this.offerProperties(mode, distance);
        this.offerListeners(new ListenerMotion(this),
        new LambdaListener<>(PacketEvent.Send.class, CPacketPlayer.Rotation.class, event -> onPacket((CPacketPlayer) event.getPacket())),
        new LambdaListener<>(PacketEvent.Send.class, CPacketPlayer.PositionRotation.class, event -> onPacket((CPacketPlayer) event.getPacket())),
        new LambdaListener<>(PacketEvent.Send.class, CPacketPlayer.Position.class, event -> onPacket((CPacketPlayer) event.getPacket())),
        new LambdaListener<>(PacketEvent.Send.class, CPacketPlayer.class, event -> onPacket((CPacketPlayer) event.getPacket())),
        new LambdaListener<>(PacketEvent.Send.class, CPacketPlayer.Rotation.class, event -> onPacket((CPacketPlayer) event.getPacket())));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        stopWatch.reset();
    }

    private void onPacket(CPacketPlayer packet) {
        if (mode.getValue() == NoFallMode.PACKET) {
            if (check()) {
                ((ICPacketPlayer) packet).setOnGround(true);
            }
        }

        if (mode.getValue() == NoFallMode.ANTI) {
            if (check()) {
                ((ICPacketPlayer) packet).setY(mc.player.posY + 0.10000000149011612);
            }
        }
    }

    protected boolean check() {
        return mc.player.fallDistance > distance.getValue();
    }
}
