package me.chachoox.lithium.impl.modules.render.storageesp;

import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerRender extends ModuleListener<StorageESP, Render3DEvent> {
    public ListenerRender(StorageESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        module.drawTileEntities();
    }
}
