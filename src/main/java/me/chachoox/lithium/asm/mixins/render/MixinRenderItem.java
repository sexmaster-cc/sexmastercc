package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.glintmodify.GlintModify;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {

    @ModifyArg(method = "renderEffect", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"))
    private int renderEffect(int glintVal) {
        final GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        return GLINT_MODIFY.isEnabled() ? GLINT_MODIFY.getEnchantColor().getRGB() : glintVal;
    }

    @ModifyArgs(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"))
    public void scaleArgsHook(Args args) {
        final GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        float scale = GLINT_MODIFY.getGlintScale();
        if (GLINT_MODIFY.isEnabled() && scale != 0) {
            args.set(0, scale);
            args.set(1, scale);
            args.set(2, scale);
        }
    }

    @ModifyArgs(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"))
    public void translateHook(Args args) {
        final GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        if (GLINT_MODIFY.isEnabled()) {
            args.set(0, (float) args.get(0) * GLINT_MODIFY.getFactor());
        }
    }

    @ModifyArgs(method = "renderEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V"))
    public void rotateHook(Args args) {
        final GlintModify GLINT_MODIFY = Managers.MODULE.get(GlintModify.class);
        if (GLINT_MODIFY.isEnabled()) {
            args.set(0, (float) args.get(0) * GLINT_MODIFY.getGlintRotate());
        }
    }
}
