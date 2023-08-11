package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.glintmodify.GlintModify;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    @Inject(method = "doRenderLayer", at = @At(value="HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).getNoArmor()) {
            ci.cancel();
        }
    }

    @Redirect(method = "renderEnchantedGlint", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 1))
    private static void renderEnchantedGlint(float red, float green, float blue, float alpha) {
        final GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        if (GLINT_MODIFY.isArmorGlint()) {
            GlStateManager.color(GLINT_MODIFY.getArmorGlintColor().getRed() / 255.0F,
                    GLINT_MODIFY.getArmorGlintColor().getGreen() / 255.0F,
                    GLINT_MODIFY.getArmorGlintColor().getBlue() / 255.0F,
                    GLINT_MODIFY.getArmorGlintColor().getAlpha());
        } else {
            GlStateManager.color(red, green, blue, alpha);
        }
    }
}
