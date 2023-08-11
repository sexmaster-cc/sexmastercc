package me.chachoox.lithium.asm.mixins.world;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient {

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void constructorHook(CallbackInfo callbackInfo) {
        Bus.EVENT_BUS.dispatch(new WorldClientEvent.Load(WorldClient.class.cast(this)));
        for (Module module : Managers.MODULE.getModules()) {
            module.onWorldLoad();
        }
    }
}
