package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.misc.RenderSkyEvent;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "renderSky(FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 3, shift = At.Shift.AFTER), cancellable = true)
    public void renderSkyHook(float f4, int f5, CallbackInfo ci) {
        final RenderSkyEvent event = new RenderSkyEvent();
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
