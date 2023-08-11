package me.chachoox.lithium.asm.mixins.network;

import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @ModifyArg(method = {"handleJoinGame", "handleRespawn"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    private GuiScreen skipTerrainScreen(GuiScreen original) {
        if (Managers.MODULE.get(NoRender.class).getLoadingScreen()) {
            return null;
        }
        return original;
    }
}