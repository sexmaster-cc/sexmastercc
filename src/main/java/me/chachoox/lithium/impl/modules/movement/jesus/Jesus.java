package me.chachoox.lithium.impl.modules.movement.jesus;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.impl.event.events.blocks.CollisionEvent;

public class Jesus extends Module implements CollisionEvent.Listener {

    protected final EnumProperty<JesusMode> mode =
            new EnumProperty<>(
                    JesusMode.TRAMPOLINE,
                    new String[]{"Mode", "Type", "method"},
                    "Solid: - Sets the current liquid you are standing on to act like a solid block " +
                            "/ Trampoline: - Jumps on the liquid like a trampoline."
            );

    protected boolean jumped;
    protected double offset;

    protected StopWatch timer = new StopWatch();
    protected final ListenerCollision listenerCollision; //wtf is this\weird phobos thing onmly activate the mixin for this or something

    public Jesus() {
        super("Jesus", new String[]{"Jesus", "WaterWalk", "LavaWalk"}, "Walk on liquids.", Category.MOVEMENT);
        this.listenerCollision = new ListenerCollision(this);
        this.offerProperties(mode);
        this.offerListeners(new ListenerCollision(this), new ListenerLiquidJump(this), new ListenerMotion(this), new ListenerTick(this));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    @Override
    public void onDisable() {
        offset = 0;
    }

    @Override
    public void onWorldLoad() {
        timer.reset();
    }

    @Override
    public void onCollision(CollisionEvent event) {
        if (this.isEnabled()) {
            listenerCollision.call(event);
        }
    }
}
