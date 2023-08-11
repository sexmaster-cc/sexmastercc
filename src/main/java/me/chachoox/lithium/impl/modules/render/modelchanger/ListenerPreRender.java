package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.impl.event.events.render.item.RenderFirstPersonEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ListenerPreRender extends ModuleListener<ModelChanger, RenderFirstPersonEvent.Pre> {
    public ListenerPreRender(ModelChanger module) {
        super(module, RenderFirstPersonEvent.Pre.class);
    }

    @Override
    public void call(RenderFirstPersonEvent.Pre event) {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate((module.translateX.getValue() * 0.5), (module.translateY.getValue() * 0.5), (module.translateZ.getValue() * 0.5));
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-(module.translateX.getValue() * 0.5), (module.translateY.getValue() * 0.5), (module.translateZ.getValue() * 0.5));
        }
    }
}
