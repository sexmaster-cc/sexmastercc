package me.chachoox.lithium.asm.mixins.item;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.misc.EatEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFood.class)
public class MixinItemFood {

    @Inject(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addStat(Lnet/minecraft/stats/StatBase;)V"))
    private void onItemUseFinishHook(ItemStack stack, World world, EntityLivingBase entity, CallbackInfoReturnable<ItemStack> info) {
        Bus.EVENT_BUS.dispatch(new EatEvent(stack, entity));
    }

}
