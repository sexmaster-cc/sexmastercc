package me.chachoox.lithium.impl.modules.movement.packetfly;

import io.netty.util.internal.ConcurrentSet;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.InvalidMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.LimitMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.util.TeleportIDs;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PacketFly extends Module {

    protected final EnumProperty<PacketFlyMode> mode =
            new EnumProperty<>(
                    PacketFlyMode.FAST,
                    new String[]{"Mode", "flightmode"}, "SetBack: - Slow packet fly, won't predict anything. /" +
                    " Factor: - Mode - \"Fast\" but you can change the speed with property \"Factor\"," +
                    " Fast: - Factor mode capped to 1.0 factor. " +
                    " Limit: - Sets your motion instead of position also sends less fly packets."
            );

    protected final EnumProperty<PhaseMode> phase =
            new EnumProperty<>(
                    PhaseMode.FULL,
                    new String[]{"Phase", "PhaseMode"},
                    "Off: - Won't phase " +
                            "/ Semi: - Clips halfway into blocks " +
                            "/ Full: - Clips fully into blocks."
            );

    protected final EnumProperty<InvalidMode> type =
            new EnumProperty<>(
                    InvalidMode.PRESERVE,
                    new String[]{"Type", "InvalidMode"},
                    "Down: - Sends invalid packet downwards" +
                            " / Up: - Sends an invalid packet upwards" +
                            " / Preserve: - Sends randomly generated numbers for the packet." +
                            " / Bounds: - Sends a packet to 255 or 0" +
                            " / LimitJitter: - Sends a packet from 29-0."
            );

    protected final EnumProperty<LimitMode> limit =
            new EnumProperty<>(
                    LimitMode.BOTH,
                    new String[]{"Limit", "LimitMode"},
                    "Tick: - Limits minecrafts ticks existed to let us know if we can packet fly." +
                            " / Speed: - Limits lag time to prevent us from getting kicked, slows down if we are gonna get kicked." +
                            " / Both: - Limits both ticks and speed." +
                            " / None: - Limits nothing, unsafe."
            );

    protected final NumberProperty<Float> factor =
            new NumberProperty<>(
                    1.0f, 0.1f, 10.0f, 0.1f,
                    new String[]{"Factor", "fac", "speed", "Sped"},
                    "Speed / factor for mode - factor."
            );

    protected final Property<Boolean> conceal =
            new Property<>(
                    false,
                    new String[]{"Conceal", "conc", "bypass"},
                    "Forces 0.624 speed."
            );

    protected final Property<Boolean> antiKick =
            new Property<>(
                    false,
                    new String[]{"AntiKick", "noKick", "ak"},
                    "Bypasses minecraft anti fly."
            );

    protected final Property<Boolean> autoClip =
            new Property<>(
                    false,
                    new String[]{"AutoClip", "AutoSneak"},
                    "Spams sneak keybind to packet fly upwards into blocks."
            );
    
    protected ConcurrentHashMap<Integer, TeleportIDs> teleportMap = new ConcurrentHashMap<>();
    protected ConcurrentSet<CPacketPlayer> packets = new ConcurrentSet<>();

    protected Random random = new Random();
    
    protected double concealOffset = 0.0624;
    
    protected int lastTpID = -1;
    protected int flightCounter = 0;
    protected int lagTime;
    
    protected boolean limited = false;
    protected boolean isSneaking = false;

    public PacketFly() {
        super("PacketFly", new String[]{"PacketFly", "Pfly", "PacketFlight"}, "Makes you fly by using packets.", Category.MOVEMENT);
        this.offerProperties(mode, phase, type, limit, factor, conceal, antiKick, autoClip);
        this.offerListeners(new ListenerUpdate(this), new ListenerMove(this), new ListenerCPacketPlayer(this), new ListenerPosLook(this), new ListenerLogout(this));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        lastTpID = -1;
        reset();

        if (autoClip.getValue() && isSneaking) {
            PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (mc.isSingleplayer()) {
            Logger.getLogger().log("<PacketFly> You can't enable this in SinglePlayer.");
            disable();
        }
    }

    @Override
    public void onDisable() {
        PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        PacketUtil.send(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        reset();
    }

    protected void reset() {
        flightCounter = 0;
        lastTpID = 0;
        lagTime = 0;
        packets.clear();
        teleportMap.clear();
    }

    protected boolean resetCounter(int counter) {
        ++flightCounter;
        if (flightCounter >= counter) {
            flightCounter = 0;
            return true;
        }
        return false;
    }

    protected void packetSender(CPacketPlayer packet) {
        packets.add(packet);
        PacketUtil.send(packet);
    }

    private int getPreserveY() {
        int y = random.nextInt(29000000);// 291k Wtf
        if (random.nextBoolean()) {
            return y;
        }
        return -y;
    }

    private int getLimitJitterY() {
        int y = random.nextInt(22) + 7;
        if (random.nextBoolean()) {
            return y;
        }
        return -y;
    }

    protected Vec3d getTeleportPos(Vec3d vecOne, Vec3d vecTwo) {
        Vec3d vec = vecOne.add(vecTwo);
        switch (type.getValue()) {
            case PRESERVE: {
                vec = vec.add(getPreserveY(), 0.0, getPreserveY());
                break;
            }
            case UP: {
                vec = vec.add(0.0, 1337.0, 0.0);
                break;
            }
            case DOWN: {
                vec = vec.add(0.0, -1337.0, 0.0);
                break;
            }
            case BOUNDS: {
                vec = new Vec3d(vec.x, mc.player.posY <= 10.0 ? 255.0 : 1.0, vec.z);
                break;
            }
            case LIMITJITTER: {
                vec = new Vec3d(0.0, getLimitJitterY(), 0.0);
                break;
            }
        }
        return vec;
    }

    protected void sendPackets(Double x, Double y, Double z, Boolean teleport) {
        Vec3d vec3d = new Vec3d(x, y, z);
        Vec3d vec3d2 = mc.player.getPositionVector().add(vec3d);
        Vec3d vec3d3 = getTeleportPos(vec3d, vec3d2);
        packetSender(new CPacketPlayer.Position(vec3d2.x, vec3d2.y, vec3d2.z, mc.player.onGround));
        packetSender(new CPacketPlayer.Position(vec3d3.x, vec3d3.y, vec3d3.z, mc.player.onGround));
        if (teleport) {
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(++lastTpID));
            teleportMap.put(lastTpID, new TeleportIDs(vec3d2.x, vec3d2.y, vec3d2.z, System.currentTimeMillis()));
        }
    }

    protected boolean checkHitBox() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    @SuppressWarnings("unused")
    protected boolean clearMap(Map.Entry<Integer, TeleportIDs> map) {
        return System.currentTimeMillis() - (map.getValue()).getY() > TimeUnit.SECONDS.toMillis(30L);
    }
}
