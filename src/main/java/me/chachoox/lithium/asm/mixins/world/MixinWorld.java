package me.chachoox.lithium.asm.mixins.world;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.customsky.CustomSky;
import me.chachoox.lithium.impl.modules.render.customsky.mode.Mode;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(value = World.class)
public class MixinWorld {

    @Inject(method = "getSkyColorBody", at = @At(value="RETURN"), cancellable = true, remap = false)
    public void getSkyColorBody(Entity entityIn, float partialTicks, CallbackInfoReturnable<Vec3d> cir) {
        final CustomSky CUSTOM_SKY = Managers.MODULE.get(CustomSky.class);
        if (CUSTOM_SKY.isEnabled() && (CUSTOM_SKY.mode.getValue() == Mode.BOTH || CUSTOM_SKY.mode.getValue() == Mode.SKY)) {
            final Color color = CUSTOM_SKY.getSkyColor();
            cir.setReturnValue(new Vec3d(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F));
        }
    }

    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    public void onEntityAdded(Entity entity, CallbackInfo callback) {
        EntityWorldEvent.Add event = new EntityWorldEvent.Add(entity);
        Bus.EVENT_BUS.dispatch(event);
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    public void removeEntity(Entity entity, CallbackInfo callback) {
        EntityWorldEvent.Remove event = new EntityWorldEvent.Remove(entity);
        Bus.EVENT_BUS.dispatch(event);
    }

    @Inject(method = "spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    public void spawnParticleHook(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).getExplosions()) {
            switch (particleType) {
                case EXPLOSION_NORMAL:
                case EXPLOSION_LARGE:
                case EXPLOSION_HUGE:
                case FIREWORKS_SPARK:
                    ci.cancel();
                default:
            }
        }
    }

    @Inject(method = "getRainStrength", at = @At(value = "HEAD"), cancellable = true)
    public void getRainStrengthHook(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (Managers.MODULE.get(NoRender.class).getWeather()) {
            callbackInfoReturnable.setReturnValue(0.0f);
        }
    }

    @Redirect(method = "getHorizon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getHorizon()D", remap = false))
    private double AlwaysZero(WorldProvider worldProvider) {
        return 0.0;
    }

}
