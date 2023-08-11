package me.chachoox.lithium.impl.modules.player.fakeplayer;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.modules.player.fakeplayer.position.Position;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

public class FakePlayer extends Module {

    protected final Property<Boolean> record =
            new Property<>(
                    false,
                    new String[]{"Record", "playrecord", "rec", "r"},
                    "Tracks all movement until this is disabled."
            );

    protected final Property<Boolean> playRecording =
            new Property<>(
                    false,
                    new String[]{"Play", "PlayRecord", "pla", "p"},
                    "Plays the tracked movement."
            );

    protected final List<Position> playerPositions = new ArrayList<>();

    public FakePlayer() {
        super("FakePlayer", new String[]{"FakePlayer", "FakeEntity", "TestPlayer", "PollosxD", "FakePlayerington"}, "Spawns a fake player into the world.", Category.PLAYER);
        this.offerProperties(record, playRecording);
        this.offerListeners(new ListenerMotion(this), new ListenerDeath(this));
    }

    @Override
    public String getSuffix() {
        return record.getValue() ? "Recording" : playRecording.getValue() ? "Playing" : null;
    }

    @Override
    public void onEnable() {
        PlayerUtil.addFakePlayerToWorld("yung_lean_fan1", -2147483647);
    }

    @Override
    public void onDisable() {
        PlayerUtil.removeFakePlayerFromWorld(-2147483647);
        playerPositions.clear();// maybe make this some clickable message for when u delete it like [ do u wanna Delte da Position Y/N ]
    }

    @Override
    public void onWorldLoad() {
        if (isEnabled()) {
            disable();
        }
    }

    public List<Position> getPlayerPositions() {
        return playerPositions;
    }
}
