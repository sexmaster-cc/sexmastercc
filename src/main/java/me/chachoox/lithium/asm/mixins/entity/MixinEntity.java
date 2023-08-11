package me.chachoox.lithium.asm.mixins.entity;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.asm.ducks.IEntity;
import me.chachoox.lithium.impl.event.events.movement.StepEvent;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.velocity.Velocity;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//move event makes it not compatible with phobos...
@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class MixinEntity implements IEntity, Minecraftable {

    @Accessor("isInWeb")
    public abstract boolean getIsInWeb();

    @Shadow
    protected EntityDataManager dataManager;
    @Shadow
    public float stepHeight;

    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationYaw;
    @Shadow
    public float rotationPitch;
    @Shadow
    public boolean onGround;

    private MoveEvent moveEvent;
    private Float prevHeight;

    @Shadow
    public World world;

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    @Shadow
    public abstract boolean isSneaking();

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void moveEntityHookHead(MoverType type, double x, double y, double z, CallbackInfo ci) {
        if (EntityPlayerSP.class.isInstance(this)) {
            this.moveEvent = new MoveEvent(type, x, y, z, this.isSneaking());
            Bus.EVENT_BUS.dispatch(this.moveEvent);
            if (moveEvent.isCanceled()) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "move", at = @At(value = "HEAD"), ordinal = 0)
    private double setX(double x) {
        return this.moveEvent != null ? this.moveEvent.getX() : x;
    }

    @ModifyVariable(method = "move", at = @At("HEAD"), ordinal = 1)
    private double setY(double y) {
        return this.moveEvent != null ? this.moveEvent.getY() : y;
    }

    @ModifyVariable(method = "move", at = @At("HEAD"), ordinal = 2)
    private double setZ(double z) {
        return this.moveEvent != null ? this.moveEvent.getZ() : z;
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.isSneaking()Z", ordinal = 0))
    public boolean isSneakingHook(Entity entity) {
        return this.moveEvent != null ? this.moveEvent.isSneaking() : entity.isSneaking();
    }

    @Inject(method = "move", at = @At("RETURN"))
    public void moveEntityHookReturn(MoverType type, double x, double y, double z, CallbackInfo info) {
        this.moveEvent = null;
    }

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        if (entity != null && (!Managers.MODULE.get(Velocity.class).doNoPush() || !entity.equals(mc.player))) {
            entity.addVelocity(x, y, z);
        }
    }

    @Inject(method = "canRenderOnFire", at = @At("HEAD"), cancellable = true)
    public void canRenderOnFireHook(CallbackInfoReturnable<Boolean> cir) {
        if (Managers.MODULE.get(NoRender.class).getEntityFire()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "move", at = @At(value = "FIELD", target = "net/minecraft/entity/Entity.onGround:Z", ordinal = 1))
    public void onGroundHook(MoverType type, double x, double y, double z, CallbackInfo info) {
        if (EntityPlayerSP.class.isInstance(this)) {
            StepEvent event = new StepEvent(Stage.PRE, this.getEntityBoundingBox(), this.stepHeight);
            Bus.EVENT_BUS.dispatch(event);
            this.prevHeight = this.stepHeight;
            this.stepHeight = event.getHeight();
        }
    }

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V"))
    public void stepCompHook(MoverType type, double x, double y, double z, CallbackInfo ci) {
        //noinspection ConstantConditions
        if (EntityPlayerSP.class.isInstance(this)) {
            StepEvent event = new StepEvent(Stage.POST, this.getEntityBoundingBox(), this.prevHeight != null ? this.prevHeight : 0.0F);
            Bus.EVENT_BUS.dispatchReversed(event, null);
        }
    }

    @Inject(method = "move", at = @At(value = "FIELD", target = "net/minecraft/entity/Entity.stepHeight:F", ordinal = 3, shift = At.Shift.BEFORE))
    public void onGroundHookComp(MoverType type, double x, double y, double z, CallbackInfo info) {
        //noinspection ConstantConditions
        if (EntityPlayerSP.class.isInstance(this)) {
            StepEvent event = new StepEvent(Stage.PRE, this.getEntityBoundingBox(), this.stepHeight);
            Bus.EVENT_BUS.dispatch(event);
            this.prevHeight = this.stepHeight;
            this.stepHeight = event.getHeight();
        }
    }


    @Inject(method = "createRunningParticles", at = @At("HEAD"), cancellable = true)
    public void createRunningParticlesHook(CallbackInfo ci) {
        if (EntityPlayerSP.class.isInstance(this) && Managers.MODULE.get(NoRender.class).getSprintParticles()) {
            ci.cancel();
        }
    }
}
