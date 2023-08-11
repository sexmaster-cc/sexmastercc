package me.chachoox.lithium.impl.event.events.render.misc;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;

public class RenderEntityEvent extends Event {
    private final Entity entity;

    public RenderEntityEvent(Entity entityIn, ICamera camera, double camX, double camY, double camZ) {
        entity = entityIn;
    }

    public Entity getEntity() {
        return entity;
    }

}

