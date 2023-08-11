package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;
import net.minecraft.network.Packet;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;

// all coded by me btw im a genius btw im the bestbtw
public class FakeLag extends Module {

    protected final ArrayList<Packet<?>> cache = new ArrayList<>();
    protected final StopWatch suffixTimer = new StopWatch();
    protected final ArrayList<Vector3d> positons = new ArrayList<>();

    protected final ColorProperty lineColor = new ColorProperty(new Color(255, 255, 255, 125), true, new String[]{"LineColor", "color", "wirecolor"});

    public FakeLag() {
        super("FakeLag", new String[]{"FakeLag", "NetFreeze", "Blink", "291kinternet"}, "Cancels packets until we disable the module.", Category.PLAYER);
        this.offerProperties(lineColor);
        this.offerListeners(new ListenerPacket(this), new ListenerPosLook(this), new ListenerRender(this));
    }

    @Override
    public String getSuffix() {
        return suffixTimer.getTime() / 100 + "";
    }

    @Override
    public void onEnable() {
        if (check()) {
            disable();
            return;
        }
        PlayerUtil.addFakePlayerToWorld(mc.player.getName(), -mc.player.getEntityId());
        clear();
    }

    @Override
    public void onDisable() {
        cache.forEach(packet -> {
            if (packet != null) {
                PacketUtil.send(packet);
            }
        });
        clear();
        PlayerUtil.removeFakePlayerFromWorld(-mc.player.getEntityId());
    }

    @Override
    public void onWorldLoad() {
        if (isEnabled()) {
            disable();
        }
    }

    protected boolean check() {
        return isNull() || mc.isSingleplayer() || mc.getConnection() == null;
    }

    protected void clear() {
        cache.clear();
        suffixTimer.reset();
        positons.clear();
    }
}
