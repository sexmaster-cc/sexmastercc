package me.chachoox.lithium.asm.mixins.item;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.blocks.PlaceBlockEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Inject(method = "Lnet/minecraft/item/ItemStack;onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;", at = @At("HEAD"))
    public void onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> callbackInfo) {
        if (worldIn.isRemote) {
            ItemStack stack = ItemStack.class.cast(this);
            if (stack.getItem() instanceof ItemBlock) {
                PlaceBlockEvent event = new PlaceBlockEvent(pos, stack);
                Bus.EVENT_BUS.dispatch(event);
            }
        }
    }
}