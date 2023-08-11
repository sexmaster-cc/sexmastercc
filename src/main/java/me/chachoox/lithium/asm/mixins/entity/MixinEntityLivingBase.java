package me.chachoox.lithium.asm.mixins.entity;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.asm.ducks.IEntityLiving;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.movement.liquid.LiquidJumpEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.noeatdelay.NoEatDelay;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity implements IEntityLiving {

    @Shadow
    @Final
    private static DataParameter<Float> HEALTH;
    @Shadow
    protected int activeItemStackUseCount;
    @Shadow
    protected ItemStack activeItemStack;

    @Override
    @Invoker("getArmSwingAnimationEnd")
    public abstract int getArmSwingAnim();

    @Inject(method = "notifyDataManagerChange", at = @At("RETURN"))
    public void notifyDataManagerChangeHook(DataParameter<?> key, CallbackInfo info) {
        if (key.equals(HEALTH) && this.dataManager.get(HEALTH) <= 0.0 && this.world != null && this.world.isRemote) {
            Bus.EVENT_BUS.dispatch(new DeathEvent(EntityLivingBase.class.cast(this)));
        }
    }

    @Redirect(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;resetActiveHand()V"))
    public void resetActiveHandHook(EntityLivingBase base) {
        if (world.isRemote && Managers.MODULE.get(NoEatDelay.class).isEnabled()
                && base instanceof EntityPlayerSP && !mc.isSingleplayer() && this.activeItemStack.getItem() instanceof ItemFood) {
            this.activeItemStackUseCount = 0;
            ((EntityPlayerSP) base).connection.sendPacket(new CPacketPlayerTryUseItem(base.getActiveHand()));
        } else {
            base.resetActiveHand();
        }
    }

    /*
    @Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
    private void getArmSwingAnimationEndHook(CallbackInfoReturnable<Integer> info) {
        final Animations ANIMATIONS = Managers.MODULE.get(Animations.class);
        if (ANIMATIONS.isEnabled()) {
            info.setReturnValue(ANIMATIONS.getSwingDelay());
        }
    }
    */

    @ModifyVariable(method = "updateItemUse", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int updateItemUse(int eatingParticlesAmount) {
        if (Managers.MODULE.get(NoRender.class).getEatingParticles()) {
            return 0;
        } else {
            return eatingParticlesAmount;
        }
    }

    @Inject(method = "handleJumpWater", at = @At("HEAD"), cancellable=true)
    private void handleJumpWater(CallbackInfo ci) {
        LiquidJumpEvent event = new LiquidJumpEvent(EntityLivingBase.class.cast(this));
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleJumpLava", at = @At("HEAD"), cancellable=true)
    private void handleJumpLava(CallbackInfo ci) {
        LiquidJumpEvent event = new LiquidJumpEvent(EntityLivingBase.class.cast(this));
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
