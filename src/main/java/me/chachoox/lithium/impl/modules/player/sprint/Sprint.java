package me.chachoox.lithium.impl.modules.player.sprint;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.settings.KeyBinding;

/**
 * @author moneymaker552
 */
public class Sprint extends Module {

    private static final int SPRINT_KEY = mc.gameSettings.keyBindSprint.getKeyCode();

    private final EnumProperty<SprintEnum> mode =
            new EnumProperty<>(
                    SprintEnum.RAGE,
                    new String[]{"Mode", "type", "sprint", "method"},
                    "Legit - Stops sprinting when certain objects are in the way. " +
                            "/ Rage - Always sprints if we are moving forward."
            );

    public Sprint() {
        super("Sprint", new String[]{"Sprint", "InstantSprint", "AutoSprint"}, "Automatically sprints depending on the situation.", Category.PLAYER);
        this.offerProperties(mode);
        this.offerListeners(new LambdaListener<>(UpdateEvent.class, event -> {
            switch (mode.getValue()) {
                case LEGIT: {
                    if (MovementUtil.isMoving() || mc.player.getFoodStats().getFoodLevel() > 6.0f || !Managers.ACTION.isSneaking() || !mc.player.collidedHorizontally) {
                        KeyBinding.setKeyBindState(SPRINT_KEY, true);
                    }
                    break;
                }
                case RAGE: {
                    if (canRageSprint()) {
                        mc.player.setSprinting(true);
                    }
                    break;
                }
            }
        }));
    }

    @Override
    public String getSuffix() {
        return mode.getFixedValue();
    }

    public boolean canRageSprint() {// first 2 args are for the mixin
        return isEnabled() && mode.getValue() == SprintEnum.RAGE && (mc.gameSettings.keyBindForward.isKeyDown()
                || mc.gameSettings.keyBindBack.isKeyDown()
                || mc.gameSettings.keyBindLeft.isKeyDown()
                || mc.gameSettings.keyBindRight.isKeyDown())
                && !(mc.player == null
                || mc.player.isSneaking()
                || mc.player.collidedHorizontally
                || mc.player.getFoodStats().getFoodLevel() <= 6f);
    }
}
