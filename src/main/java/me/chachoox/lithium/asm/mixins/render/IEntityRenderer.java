package me.chachoox.lithium.asm.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface IEntityRenderer {

    @Invoker("orientCamera")
    void invokeOrientCamera(float partialTicks);
}
