package me.chachoox.lithium.impl.event.events.screen;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.client.gui.GuiScreen;

public class GuiScreenEvent<T extends GuiScreen> extends Event {
    private final T screen;

    public GuiScreenEvent(T screen) {
        this.screen = screen;
    }

    public T getScreen() {
        return screen;
    }

}