package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
public class MixinRenderItemFrame {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).getItemFrames()) {
            ci.cancel();
        }
    }

}
