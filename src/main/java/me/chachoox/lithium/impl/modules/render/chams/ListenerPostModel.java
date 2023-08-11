package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.model.ModelRenderEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;

public class ListenerPostModel extends ModuleListener<Chams, ModelRenderEvent.Post> {
    public ListenerPostModel(Chams module) {
        super(module, ModelRenderEvent.Post.class);
    }

    @Override
    public void call(ModelRenderEvent.Post event) {
        if (!module.normal.getValue()) {
            final Entity entity = event.getEntity();
            if (module.playerWires.getValue()) {
                if (event.getEntity() == mc.player && !module.self.getValue()) {
                    return;
                }
                module.onWireframeModel(event.getModel(), entity, event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }
        }
    }
}
