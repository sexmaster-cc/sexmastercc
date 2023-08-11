package me.chachoox.lithium.impl.modules.render.norender;

import me.chachoox.lithium.impl.event.events.render.misc.RenderEntityEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerRenderEntity extends ModuleListener<NoRender, RenderEntityEvent> {
    public ListenerRenderEntity(NoRender module) {
        super(module, RenderEntityEvent.class);
    }

    @Override
    public void call(RenderEntityEvent event) {

        if (event.getEntity() instanceof EntityParrot && module.getNoParrots()) {
            event.setCanceled(true);
        }

        if (event.getEntity() instanceof EntityPlayer && event.getEntity().isInvisible() && module.getNoSpectators()) {
            event.setCanceled(true);
        }

        if (event.getEntity() instanceof EntityTNTPrimed && module.getTnt()) {
            mc.world.removeEntity(event.getEntity());
            event.setCanceled(true);
        }
    }
}
