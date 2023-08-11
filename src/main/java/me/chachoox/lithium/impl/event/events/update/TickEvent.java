package me.chachoox.lithium.impl.event.events.update;

import me.chachoox.lithium.api.interfaces.Minecraftable;

public class TickEvent implements Minecraftable {

    public boolean isSafe() {
        return mc.player != null && mc.world != null;
    }

    public static final class PostWorldTick extends TickEvent {
    }

    public static final class Post extends TickEvent {
    }

}
