package me.chachoox.lithium.impl.modules.player.autotool;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;

public class AutoTool extends Module {

    protected final NumberProperty<Integer> swapTicks =
            new NumberProperty<>(
                    7, 0, 20,
                    new String[]{"SwapTicks", "SwaptoTicks", "stick"},
                    "The time it will take to switch to the tool."
            );

    protected final NumberProperty<Integer> swapBackTicks =
            new NumberProperty<>(
                    3, 0, 20,
                    new String[]{"SwapBackTicks", "resetTick", "btick"},
                    "The time it will take to switch back to the original slot."
            );

    protected int lastSlot = -1;
    protected boolean set = false;

    protected StopWatch swapTimer = new StopWatch();
    protected StopWatch swapBackTimer = new StopWatch();

    public AutoTool() {
        super("AutoTool", new String[]{"AutoTool", "AutoToolSwitch", "FastTool"}, "Automatically switches to the best tool for the block you are destroying.", Category.PLAYER);
        this.offerListeners(new ListenerDamageBlock(this), new ListenerUpdate(this),
                new LambdaListener<>(DisconnectEvent.class, event -> mc.addScheduledTask(this::reset)),
                new LambdaListener<>(DeathEvent.class, this::onDeath));
        this.offerProperties(swapBackTicks, swapTicks);
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onWorldLoad() {
        reset();
    }

    protected void reset() {
        lastSlot = -1;
        set = false;
        swapTimer.reset();
        swapBackTimer.reset();
    }

    private void onDeath(DeathEvent event) {
        if (event.getEntity() == mc.player) {
            this.reset();
        }
    }
}
