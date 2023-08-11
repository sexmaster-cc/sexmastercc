package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    protected void renderPortalHook(float n, ScaledResolution scaledResolution, CallbackInfo info) {
        if (Managers.MODULE.get(NoRender.class).getPortal()) {
            info.cancel();
        }
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo info) {
        if (Managers.MODULE.get(NoRender.class).getPumpkin()) {
            info.cancel();
        }
    }

    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
        if (Managers.MODULE.get(Hud.class).isEnabled()) {
            info.cancel();
        }
    }
}