package me.chachoox.lithium.impl.event.events.render.item;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.EnumHandSide;

public class RenderFirstPersonEvent extends Event {
    private final EnumHandSide enumHandSide;

    public RenderFirstPersonEvent(EnumHandSide handSide) {
        this.enumHandSide = handSide;
    }

    public EnumHandSide getHandSide() {
        return enumHandSide;
    }

    public static class Pre extends RenderFirstPersonEvent {
        public Pre(EnumHandSide enumHandSide) {
            super(enumHandSide);
        }
    }

    public static class Post extends RenderFirstPersonEvent {
        public Post(EnumHandSide enumHandSide) {
            super(enumHandSide);
        }
    }
}
