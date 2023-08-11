package me.chachoox.lithium.asm.mixins.entity;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.movement.actions.JumpEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "jump", at = @At("HEAD"))
    public void onJump(CallbackInfo ci) {
        if ((Object) this == Minecraft.getMinecraft().player) {
            Bus.EVENT_BUS.dispatch(new JumpEvent());
        }
    }

}
