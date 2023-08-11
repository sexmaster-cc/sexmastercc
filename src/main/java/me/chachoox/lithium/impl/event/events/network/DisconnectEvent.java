package me.chachoox.lithium.impl.event.events.network;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.text.ITextComponent;

public class DisconnectEvent extends Event {
    private final ITextComponent component;

    public DisconnectEvent(ITextComponent component) {
        this.component = component;
    }

    public ITextComponent getComponent() {
        return component;
    }

}
