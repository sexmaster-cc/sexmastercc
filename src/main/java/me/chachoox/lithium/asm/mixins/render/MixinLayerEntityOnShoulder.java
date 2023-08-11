package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerEntityOnShoulder.class)
public class MixinLayerEntityOnShoulder {

    @Inject(method = "doRenderLayer", at = @At(value = "HEAD"), cancellable = true)
    public void doRenderLayerHook(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo info) {
        if (Managers.MODULE.get(NoRender.class).isEnabled() && Managers.MODULE.get(NoRender.class).getNoParrots()) {
            info.cancel();
        }
    }

}
