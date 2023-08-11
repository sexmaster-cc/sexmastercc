package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.misc.BlockRenderEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public abstract class MixinBlockRendererDispatcher {

    @Inject(method = "renderBlock", at = @At("HEAD"))
    public void renderBlockHook(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> info) {
        Bus.EVENT_BUS.dispatch(new BlockRenderEvent(pos, state));
    }
}
