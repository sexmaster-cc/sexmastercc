package me.chachoox.lithium.impl.modules.movement.noslow;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.asm.ducks.IEntity;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noslow.util.AntiWebMode;

public class NoSlow extends Module {

    protected final Property<Boolean> items =
            new Property<>(
                    true,
                    new String[]{"Input", "inputs", "food", "potion", "bow"},
                    "Stops slowdown for inputs."
            );

    protected final Property<Boolean> soul =
            new Property<>(
                    false,
                    new String[]{"Soul", "Soulsand"},
                    "Stops slowdown for soul sand."
            );

    protected final Property<Boolean> slime =
            new Property<>(
                    false,
                    new String[]{"Slime", "Slimeblocks"},
                    "Stops slowdown for slime blocks."
            );

    protected final Property<Boolean> strict =
            new Property<>(
                    false,
                    new String[]{"Strict", "NCPStrict", "NcpBypass"},
                    "Bypasses NCP updated for inputs."
            );

    protected final EnumProperty<AntiWebMode> antiWeb =
            new EnumProperty<>(
                    AntiWebMode.MOTION,
                    new String[]{"AntiWeb", "WebSpeed", "aw"},
                    "Modifies speed in webs."
            );

    protected final NumberProperty<Float> speed =
            new NumberProperty<>(
                    1.0f, 1.0f, 20.0f, 0.1f,
                    new String[]{"Speed", "Sped", "AntiWebSpeed"},
                    "How fast we want to go down in webs."
            );

    protected boolean timerCheck;

    public NoSlow() {
        super("NoSlow", new String[]{"NoSlow", "NoSlowDown", "NoStop", "AntiWeb", "NoWeb"}, "Prevents multiple occasions that make you slow down.", Category.MOVEMENT);
        this.offerProperties(items, soul, slime, strict, antiWeb, speed);
        this.offerListeners(new ListenerInput(this), new ListenerTryUseItemOnBlock(this), new ListenerTryUseItem(this),
                new ListenerUpdate(this), new ListenerSneak(this), new ListenerMove(this));
    }

    @Override
    public void onEnable() {
        timerCheck = false;
    }

    @Override
    public void onDisable() {
        Managers.TIMER.reset();
    }

    protected boolean doWeb() {
        return ((IEntity) mc.player).getIsInWeb() && mc.gameSettings.keyBindSneak.isKeyDown() && !mc.player.noClip;
    }

    public Boolean noSoul() {
        return isEnabled() && soul.getValue();
    }

    public Boolean noSlime() {
        return isEnabled() && slime.getValue();
    }

}
