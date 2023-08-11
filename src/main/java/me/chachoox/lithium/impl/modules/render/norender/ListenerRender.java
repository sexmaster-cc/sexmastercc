package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerRender extends ModuleListener<NoRender, Render3DEvent> {
    public ListenerRender(NoRender module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity.equals(mc.player)) {
                return;
            }
            if (module.getLimbSwing()) {
                entity.limbSwing = 0;
                entity.limbSwingAmount = 0;
                entity.prevLimbSwingAmount = 0;
            }
        }
    }
}
