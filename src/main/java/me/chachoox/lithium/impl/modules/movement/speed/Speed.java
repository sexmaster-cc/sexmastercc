package me.chachoox.lithium.impl.modules.movement.speed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.movement.KnockbackManager;
import me.chachoox.lithium.impl.modules.movement.speed.enums.JumpMode;
import me.chachoox.lithium.impl.modules.movement.speed.enums.SpeedMode;

public class Speed extends Module {

    protected final EnumProperty<SpeedMode> mode =
            new EnumProperty<>(
                    SpeedMode.STRAFE,
                    new String[]{"Mode", "type", "method"},
                    "Strafe: - Better sprint jumping with more air control and speed (Max Speed - 28km / Strict - 27-25km)"
                            + " / OnGround: - Simulates jumping by modifying packets (Max Speed - 150km)"
            );

    protected final EnumProperty<JumpMode> jump =
            new EnumProperty<>(
                    JumpMode.LOW,
                    new String[]{"Jump", "AutoJump", "AutoJumpMode", "AETRA"},
                    "Low: - Jumps slightly lower but can cause you to get flagged " +
                            "/ Vanilla: - Uses vanilla jump height, will slow down strafe."
            );

    protected final Property<Boolean> kbBoost =
            new Property<>(
                    false,
                    KnockbackManager.KB_BOOST_ALIAS,
                    KnockbackManager.KB_BOOST_DESCRIPTION
            );

    protected final NumberProperty<Float> boostReduction =
            new NumberProperty<>(
                    4.5f, 1.0f, 5.0f, 0.1f,
                    KnockbackManager.BOOST_REDUCTION_ALIAS,
                    KnockbackManager.BOOST_REDUCTION_DESCRIPTION
            );

    protected final Property<Boolean> inLiquids =
            new Property<>(
                    false,
                    new String[]{"InLiquids", "SpeedinWater", "spedInWater", "SpeedINLava"},
                    "Uses speed in liquids."
            );

    protected final Property<Boolean> useTimer =
            new Property<>(
                    false,
                    new String[]{"UseTimer", "Timer"},
                    "Changes the tick speed a little bit to make you go faster."
            );

    protected final Property<Boolean> autoSprint =
            new Property<>(
                    false,
                    new String[]{"AutoSprint", "sprint", "auto", "sprin"},
                    "Automatically sprints when strafing."
            );


    protected double distance;
    protected double lastDist;
    protected boolean boost;
    protected double speed;
    protected double strictTicks;
    protected int strafeStage;
    protected int onGroundStage;

    public Speed() {
        super("Speed", new String[]{"Speed", "Sped", "FastRun", "FastMove", "PollosRunningFromHisParents"}, "Allows you to move faster.", Category.MOVEMENT);
        this.offerListeners(new ListenerMotion(this), new ListenerMove(this), new ListenerPosLook(this));
        this.offerProperties(mode, jump, kbBoost, boostReduction, inLiquids, useTimer, autoSprint);
        this.useTimer.addObserver(event -> Managers.TIMER.reset());
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            speed = MovementUtil.getSpeed();
            distance = MovementUtil.getDistance2D();
        }
        strafeStage = 4;
        lastDist = 0;
        onGroundStage = 2;
    }

    @Override
    public void onDisable() {
        Managers.TIMER.reset();
    }

    protected boolean canSprint() {
        return (mc.gameSettings.keyBindForward.isKeyDown()
                || mc.gameSettings.keyBindBack.isKeyDown()
                || mc.gameSettings.keyBindLeft.isKeyDown()
                || mc.gameSettings.keyBindRight.isKeyDown())
                && !(mc.player == null
                || mc.player.isSneaking()
                || mc.player.collidedHorizontally
                || mc.player.getFoodStats().getFoodLevel() <= 6f);
    }
}
