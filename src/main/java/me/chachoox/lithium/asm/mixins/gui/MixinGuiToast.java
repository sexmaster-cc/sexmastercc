package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.toasts.GuiToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiToast.class)
public class MixinGuiToast {

    @Inject(method = "drawToast", at = @At("HEAD"), cancellable = true)
    public void drawToast(ScaledResolution resolution, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).getToast()) {
            ci.cancel();
        }
    }

}
