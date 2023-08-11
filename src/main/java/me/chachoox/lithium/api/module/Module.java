package me.chachoox.lithium.api.module;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.api.Subscriber;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.render.animation.Animation;
import me.chachoox.lithium.api.util.render.animation.DecelerateAnimation;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.hud.Hud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Module implements Subscriber, Minecraftable, Labeled {
    public final List<Listener<?>> listeners = new ArrayList<>();
    public final List<Property<?>> properties = new ArrayList<>();

    public final StringProperty displayLabel = new StringProperty(null, new String[]{"DisplayLabel", "label"});
    public final Property<Boolean> enabled = new Property<>(false, new String[]{"Enabled", "enabl"}, "Current state of the module.");
    public final Property<Boolean> drawn = new Property<>(true, new String[]{"Drawn", "draw", "hide"}, "Display the module in the arraylist"); // cant we just movew these to booleans
    public final BindProperty bind = new BindProperty(new Bind(-1), new String[]{"Keybind", "bind", "b"}, "Current bind of the module.");

    private final Animation animation = new DecelerateAnimation(250, 1);

    private final String label;
    private final String description;
    private final String[] aliases;
    private final Category category;

    public Module(String label, String[] aliases, String description, Category category) {
        this.label = label;
        this.category = category;
        this.aliases = aliases;
        this.description = description;
        this.offerProperties(displayLabel, enabled, drawn, bind);
    }

    public void offerProperties(Property<?>... properties) {
        Collections.addAll(this.properties, properties);
    }

    public void offerListeners(Listener<?>... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    public Collection<Property<?>> getProperties() {
        return properties;
    }

    @SuppressWarnings("rawtypes")
    public Property getProperty(String alias) {
        for (Property<?> property : properties) {
            for (String aliases : property.getAliases()) {
                if (!alias.equalsIgnoreCase(aliases)) continue;
                return property;
            }
        }
        return null;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            enable();
        } else {
            disable();
        }
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public void toggle() {
        setEnabled(!enabled.getValue());
    }

    public void enable() {
        enabled.setValue(true);
        sendToggleMessage(this);
        if (!Bus.EVENT_BUS.isSubscribed(this)) {
            Bus.EVENT_BUS.subscribe(this);
        }
        onEnable();
    }

    public void enableNoMessage() {
        enabled.setValue(true);
        if (!Bus.EVENT_BUS.isSubscribed(this)) {
            Bus.EVENT_BUS.subscribe(this);
        }
        onEnable();
    }

    public void disable() {
        enabled.setValue(false);
        sendToggleMessage(this);
        onDisable();
        Bus.EVENT_BUS.unsubscribe(this);
    }

    public void disableNoMessage() {
        enabled.setValue(false);
        onDisable();
        Bus.EVENT_BUS.unsubscribe(this);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onWorldLoad() {
    }

    public void onLoad() {
    }

    public boolean isNull() {
        return mc.player == null || mc.world == null;
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    public boolean isHidden() {
        return !drawn.getValue();
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return bind.getValue().getKey();
    }

    public void setKey(int key) {
        this.bind.getValue().setKey(key);
    }

    public String getSuffix() {
        return null;
    }

    public String getFullLabel() {
        return displayLabel.getValue() + (getSuffix() != null ? brackets() + " [" + TextColor.WHITE + getSuffix() + brackets() + "]" : "");
    }

    public String getDescription() {
        return description;
    }

    public Animation getAnimation() {
        return animation;
    }

    @Override
    public Collection<Listener<?>> getListeners() {
        return listeners;
    }

    private String brackets() {
        if (Managers.MODULE.get(Hud.class).whiteBrackets()) {
            return TextColor.WHITE;
        } else {
            return TextColor.GRAY;
        }
    }

    public void sendToggleMessage(Module module) {
        if (!(module instanceof ClickGUI)) {
            if (mc.ingameGUI != null && mc.player != null && Managers.MODULE.get(Hud.class).isAnnouncingModules()) {
                Logger.getLogger().log(TextColor.DARK_AQUA + module.displayLabel.getValue()
                        + TextColor.LIGHT_PURPLE
                        + " was "
                        + (enabled.getValue() ? TextColor.GREEN : TextColor.RED)
                        + (enabled.getValue() ? "enabled" : "disabled"), 4444);
            }
        }
    }
}