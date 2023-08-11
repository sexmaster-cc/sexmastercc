package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.impl.event.events.render.item.RenderFirstPersonEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ListenerPostRender extends ModuleListener<ModelChanger, RenderFirstPersonEvent.Post> {
    public ListenerPostRender(ModelChanger module) {
        super(module, RenderFirstPersonEvent.Post.class);
    }

    @Override
    public void call(RenderFirstPersonEvent.Post event) {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.rotate((module.rotateY.getValue() * 2), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((module.rotateX.getValue() * 2), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((module.rotateZ.getValue() * 2), 0.0f, 0.0f, 1.0f);
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.rotate(-(module.rotateY.getValue() * 2), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((module.rotateX.getValue() * 2), 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(module.rotateZ.getValue() * 2), 0.0f, 0.0f, 1.0f);
        }
    }
}
