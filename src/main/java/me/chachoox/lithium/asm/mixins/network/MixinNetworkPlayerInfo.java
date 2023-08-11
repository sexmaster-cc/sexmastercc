package me.chachoox.lithium.asm.mixins.network;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {

    @Shadow
    public GameProfile gameProfile;

    @Shadow
    Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = Maps.newEnumMap(MinecraftProfileTexture.Type.class);

    @Shadow
    public abstract void loadPlayerTextures();

    @Shadow
    public abstract String getSkinType();

    @Overwrite
    public ResourceLocation getLocationSkin() {
        this.loadPlayerTextures();
        final NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        if (NAME_PROTECT.isFakeSkin() && gameProfile.getName().equals(Minecraft.getMinecraft().player.getName())) {
            if (getSkinType().equalsIgnoreCase("slim")) {
                return new ResourceLocation("textures/entity/alex.png");
            }
            return new ResourceLocation("textures/entity/steve.png");
        }
        return MoreObjects.firstNonNull(this.playerTextures.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

}
