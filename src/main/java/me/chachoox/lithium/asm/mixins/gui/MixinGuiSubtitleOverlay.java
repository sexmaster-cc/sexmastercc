package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSubtitleOverlay.class)
public abstract class MixinGuiSubtitleOverlay {

    @Inject(method = "renderSubtitles", at = @At(value = "HEAD"))
    public void renderSubtitlesHook(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new Render2DEvent());
    }

}

