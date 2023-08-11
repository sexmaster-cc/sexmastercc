package me.chachoox.lithium.asm.mixins.entity;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.asm.ducks.IEntityPlayerSP;
import me.chachoox.lithium.impl.event.events.blocks.BlockPushEvent;
import me.chachoox.lithium.impl.event.events.movement.InputUpdateEvent;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.sprint.Sprint;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinEntityLivingBase implements IEntityPlayerSP {

    @Override
    @Accessor("lastReportedYaw")
    public abstract void setLastReportedYaw(float lastReportedYaw);

    @Override
    @Accessor("lastReportedPitch")
    public abstract void setLastReportedPitch(float lastReportedPitch);

    private MotionUpdateEvent motionEvent;

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;" + "onUpdate()V", shift = At.Shift.BEFORE))
    public void onUpdateHook(CallbackInfo info) {
        Bus.EVENT_BUS.dispatch(new UpdateEvent());
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void onUpdateWalkingPlayerHead(CallbackInfo callbackInfo) {
        motionEvent = new MotionUpdateEvent(Stage.PRE, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        Bus.EVENT_BUS.dispatch(motionEvent);
        if (motionEvent.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At(value = "HEAD"), cancellable = true)
    public void pushOutOfBlocksHook(CallbackInfoReturnable<Boolean> cir) {
        BlockPushEvent event = new BlockPushEvent();
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            cir.cancel();
        }
    }

    @ModifyArg(method = "setSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;setSprinting(Z)V"))
    public boolean setSprintingHook(boolean sprinting) {
        if (Managers.MODULE.get(Sprint.class).canRageSprint()) {
            return true;
        }
        return sprinting;
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At(value = "RETURN"))
    private void onUpdateWalkingPlayerReturn(CallbackInfo callbackInfo) {
        MotionUpdateEvent event = new MotionUpdateEvent(Stage.POST, motionEvent);
        event.setCanceled(motionEvent.isCanceled());
        Bus.EVENT_BUS.dispatchReversed(event, null);
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;updatePlayerMoveState()V"))
    public void updatePlayerMoveState(MovementInput input) {
        input.updatePlayerMoveState();
        InputUpdateEvent event = new InputUpdateEvent(input);
        Bus.EVENT_BUS.dispatch(event);
    }
}
