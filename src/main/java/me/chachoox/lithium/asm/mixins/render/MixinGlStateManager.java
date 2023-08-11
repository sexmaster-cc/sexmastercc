package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.pollosesp.PollosESP;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {
    @ModifyArgs(method = "color(FFFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private static void modifyColor4fArgs(Args args) {
        PollosESP POLLOS_ESP = Managers.MODULE.get(PollosESP.class);
        if (POLLOS_ESP != null && POLLOS_ESP.isSeizure()) {
            args.set(0, (float) Math.random());
            args.set(1, (float) Math.random());
            args.set(2, (float) Math.random());
        }
    }

    @ModifyArgs(method = "glTexCoord2f", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTexCoord2f(FF)V"))
    private static void modifyTexCoord2fArgs(Args args) {
        PollosESP POLLOS_ESP = Managers.MODULE.get(PollosESP.class);
        if (POLLOS_ESP != null && POLLOS_ESP.isSeizure()) {
            double rand = 0.04;
            args.set(0, (Float) args.get(0) + (float) ((Math.random() * rand) - (rand / 2)));
            args.set(1, (Float) args.get(1) + (float) ((Math.random() * rand) - (rand / 2)));
        }
    }

    @ModifyArgs(method = "glVertex3f", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glVertex3f(FFF)V"))
    private static void modifyVectex3fArgs(Args args) {
        PollosESP POLLOS_ESP = Managers.MODULE.get(PollosESP.class);
        if (POLLOS_ESP != null && POLLOS_ESP.isSeizure()) {
            double rand = 0.01;
            args.set(0, (Float) args.get(0) + (float) ((Math.random() * rand) - (rand / 2)));
            args.set(1, (Float) args.get(1) + (float) ((Math.random() * rand) - (rand / 2)));
            args.set(2, (Float) args.get(1) + (float) ((Math.random() * rand) - (rand / 2)));
        }
    }
}