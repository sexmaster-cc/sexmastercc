package me.chachoox.lithium.impl.modules.movement.noclip;

import io.netty.util.internal.ConcurrentSet;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class NoClip extends Module {

    protected final Set<CPacketPlayer> packets = new ConcurrentSet<>();

    protected final NumberProperty<Float> phaseSpeed =
            new NumberProperty<>(
                    0.42f, 0.0f, 1.0f, 0.05f,
                    new String[]{"PhaseSpeed", "Speed", "Factor", "Sped"},
                    "How fast we want to clip."
            );

    protected final Property<Boolean> adjustMotion =
            new Property<>(
                    true,
                    new String[]{"MotionAdjust", "MotionAdj", "Motion"},
                    "Sets your position to the same as your motion."
            );

    protected final Property<Boolean> sendPackets =
            new Property<>(
                    true,
                    new String[]{"SendPacket", "SendPosition", "PositionSet"},
                    "Sets an extra position packet."
            );

    protected final Property<Boolean> cancelPackets =
            new Property<>(false,
                    new String[]{"CancelPackets", "Cancel", "Canceller", "PacketCancel"},
                    "Limits the amount of packets we send (stops packet kicks)."
            );

    protected final Property<Boolean> setPos =
            new Property<>(
                    true,
                    new String[]{"SetPosition", "PosSet", "Reposition"},
                    "Alternative way of setting position, uses minecraft method instead of packet method."
            );

    protected final Property<Boolean> shift =
            new Property<>(
                    true,
                    new String[]{"Shift", "Sneaked"},
                    "Uses sneaking offset instead of normal offset."
            );

    protected final Property<Boolean> removeHitbox =
            new Property<>(
                    true,
                    new String[]{"RemoveHitbox", "HitboxRemove", "NoHitbox"},
                    "Removes block hitbox when moving."
            );

    protected StopWatch timer = new StopWatch();

    public NoClip() {
        super("NoClip", new String[]{"NoClip", "Phase", "CornerClip", "Clip"}, "Phases you into half of a block to take less damage.", Category.MOVEMENT);
        this.offerListeners(new ListenerMove(this), new ListenerCPlayer(this), new ListenerUpdate(this));
        this.offerProperties(phaseSpeed, adjustMotion, sendPackets, cancelPackets, setPos, shift, removeHitbox);
    }

    public void onEnable() {
        timer.reset();
    }

    protected double[] getMotion() {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double) moveForward * 0.031 * -Math.sin(Math.toRadians(rotationYaw)) + (double) moveStrafe * 0.031 * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double) moveForward * 0.031 * Math.cos(Math.toRadians(rotationYaw)) - (double) moveStrafe * 0.031 * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    protected void sendPackets(double x, double y, double z) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = mc.player.getPositionVector().add(vec);
        if (sendPackets.getValue()) {
            packetSender(new CPacketPlayer.Position(position.x, position.y, position.z, mc.player.onGround));
        }
        if (setPos.getValue()) {
            mc.player.setPosition(position.x, position.y, position.z);
        }
    }

    private void packetSender(CPacketPlayer packet) {
        packets.add(packet);
        PacketUtil.send(packet);
    }

    protected boolean checkHitBoxes() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }
}
