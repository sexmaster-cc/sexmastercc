package me.chachoox.lithium.asm.mixins.client;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import me.chachoox.lithium.impl.event.events.blocks.BlockDestroyEvent;
import me.chachoox.lithium.impl.event.events.blocks.BreakBlockEvent;
import me.chachoox.lithium.impl.event.events.blocks.ClickBlockEvent;
import me.chachoox.lithium.impl.event.events.blocks.DamageBlockEvent;
import me.chachoox.lithium.impl.event.events.misc.RightClickItemEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//this break compatibility with phobos...
@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP implements IPlayerControllerMP {

    @Shadow
    private float curBlockDamageMP;

    @Shadow
    private int blockHitDelay;

    @Override
    @Invoker("syncCurrentPlayItem")
    public abstract void syncItem();

    @Override
    @Accessor("blockHitDelay")
    public abstract void setBlockHitDelay(int delay);

    @Override
    @Accessor("isHittingBlock")
    public abstract void setIsHittingBlock(boolean b);

    @Override
    @Accessor("curBlockDamageMP")
    public abstract void setCurBlockDamageMP(float damage);

    @Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V"), cancellable = true)
    public void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BreakBlockEvent breakEvent = new BreakBlockEvent(pos);
        Bus.EVENT_BUS.dispatch(breakEvent);

        if (breakEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "clickBlock", at = @At(value = "HEAD"), cancellable = true)
    public void clickBlockHook(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> info) {
        ClickBlockEvent event = new ClickBlockEvent(pos, facing);
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(BlockPos pos, EnumFacing facing, CallbackInfoReturnable<Boolean> cir) {
        DamageBlockEvent event = new DamageBlockEvent(pos, facing, this.curBlockDamageMP, this.blockHitDelay);
        Bus.EVENT_BUS.dispatch(event);

        this.curBlockDamageMP = event.getDamage();
        this.blockHitDelay = event.getDelay();

        if (event.isCanceled()) {
            cir.cancel();
        }
    }

    @Inject(method = "processRightClick", at = @At("HEAD"), cancellable = true)
    public void processClickHook(EntityPlayer player, World worldIn, EnumHand hand, CallbackInfoReturnable<EnumActionResult> info) {
        RightClickItemEvent event = new RightClickItemEvent(player, worldIn, hand);

        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "processRightClickBlock", at = @At(value = "HEAD"), cancellable = true)
    public void clickBlockHook(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> info) {
        ClickBlockEvent.Right event = new ClickBlockEvent.Right(pos, direction, vec, hand);
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Dynamic
    @Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "net/minecraft/block/Block.removedByPlayer" + "(Lnet/minecraft/block/state/IBlockState;" + "Lnet/minecraft/world/World;" + "Lnet/minecraft/util/math/BlockPos;" + "Lnet/minecraft/entity/player/EntityPlayer;Z)Z", remap = false), cancellable = true)
    private void onPlayerDestroyBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockDestroyEvent event = new BlockDestroyEvent(Stage.PRE, pos);
        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "onPlayerDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlayerDestroy(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V", shift = At.Shift.BEFORE))
    private void onPlayerDestroyHook(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Bus.EVENT_BUS.dispatch(new BlockDestroyEvent(Stage.POST, pos));
    }

}
