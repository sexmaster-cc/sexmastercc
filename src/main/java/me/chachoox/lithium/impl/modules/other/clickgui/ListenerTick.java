package me.chachoox.lithium.impl.modules.other.clickgui;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;

public class ListenerTick extends ModuleListener<ClickGUI, TickEvent> {
    public ListenerTick(ClickGUI module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!(mc.currentScreen instanceof SexMasterGui)) {
            module.setEnabled(false);
        }
    }
}
