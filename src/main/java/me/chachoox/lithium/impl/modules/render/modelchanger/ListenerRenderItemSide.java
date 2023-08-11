package me.chachoox.lithium.impl.modules.render.modelchanger;

import me.chachoox.lithium.impl.event.events.render.item.RenderItemSideEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerRenderItemSide extends ModuleListener<ModelChanger, RenderItemSideEvent> {
    public ListenerRenderItemSide(ModelChanger module) {
        super(module, RenderItemSideEvent.class);
    }

    @Override
    public void call(RenderItemSideEvent event) {
        event.setX((module.scaleX.getValue()));
        event.setY((module.scaleY.getValue()));
        event.setZ((module.scaleZ.getValue()));
    }
}
