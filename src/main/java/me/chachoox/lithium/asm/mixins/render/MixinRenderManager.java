package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.misc.RenderEntityEvent;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public void shouldRenderHook(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> ci) {
        RenderEntityEvent event = new RenderEntityEvent(entityIn, camera, camX, camY, camZ);
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            ci.setReturnValue(false);
        }
    }
}
