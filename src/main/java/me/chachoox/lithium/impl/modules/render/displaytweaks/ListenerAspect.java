package me.chachoox.lithium.impl.modules.render.displaytweaks;

import me.chachoox.lithium.impl.event.events.screen.AspectRatioEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerAspect extends ModuleListener<DisplayTweaks, AspectRatioEvent> {
    public ListenerAspect(DisplayTweaks module) {
        super(module, AspectRatioEvent.class);
    }

    @Override
    public void call(AspectRatioEvent event) {
        if (module.aspectRatio.getValue()) {
            event.setAspectRatio(module.aspectRatioWidth.getValue() / (float) module.aspectRatioHeight.getValue());
        }
    }
}