package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelEnderCrystal.class)
public abstract class MixinModelEnderCrystal {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"))
    public void render(float x, float y, float z) {
        final Chams CHAMS = Managers.MODULE.get(Chams.class);
        float crystalScale = CHAMS.smallScale.getValue();
        if (CHAMS.isEnabled()) {
            GlStateManager.scale(x + crystalScale, y + crystalScale, z + crystalScale);
        } else {
            GlStateManager.scale(x, y, z);
        }
    }
}