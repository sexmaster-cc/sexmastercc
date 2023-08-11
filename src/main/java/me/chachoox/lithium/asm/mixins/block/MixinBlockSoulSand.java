package me.chachoox.lithium.asm.mixins.block;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noslow.NoSlow;
import net.minecraft.block.BlockSoulSand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
public abstract class MixinBlockSoulSand {

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void onEntityCollisionHook(CallbackInfo info) {
        if (Managers.MODULE.get(NoSlow.class).noSoul()) {
            info.cancel();
        }
    }
}