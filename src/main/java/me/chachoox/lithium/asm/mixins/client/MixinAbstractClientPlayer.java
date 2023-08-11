package me.chachoox.lithium.asm.mixins.client;

import com.mojang.authlib.GameProfile;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.managers.minecraft.CapesManager;
import me.chachoox.lithium.impl.modules.render.fovmodifier.FovModifier;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer implements Minecraftable {
    public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Shadow
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        NetworkPlayerInfo playerInfo = getPlayerInfo();
        if (playerInfo != null && Managers.CAPES.isCapesEnabled()) {
            ResourceLocation location = CapesManager.getResourceLocation(playerInfo.getGameProfile().getId());
            if (location != null) {
                callbackInfoReturnable.setReturnValue(location);
            }
        }
    }

    @Inject(method = "getFovModifier", at = @At("HEAD"), cancellable = true)
    public void getFovModifierHook(CallbackInfoReturnable<Float> info) {
        if (Managers.MODULE.get(NoRender.class).getDynamicFov()) {
            info.setReturnValue(1.0f);
        } else if (Managers.MODULE.get(FovModifier.class).isEnabled()) {
            float f = 1.0F;
            float add = 0;
            float ten = 10;// deadass didnt wanna keep typing the number
            if (this.capabilities.isFlying) {
                f *= 1 + FovModifier.get().fly() / ten;
            }

            boolean sprint = this.isSprinting();
            boolean speed = this.isPotionActive(MobEffects.SPEED);
            boolean slow = this.isPotionActive(MobEffects.SLOWNESS);

            if (sprint) add = add + FovModifier.get().sprint() / ten;
            if (slow) add = add + FovModifier.get().slow() / ten;
            if (speed) add = add + FovModifier.get().swiftness() / ten;

            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

            if (FovModifier.get().sprint() == 0.0f && sprint) {
                info.setReturnValue(1.0f);
                return;
            }

            if (FovModifier.get().slow() == 0.0f && slow) {
                info.setReturnValue(1.0f);
                return;
            }

            if (FovModifier.get().swiftness() == 0.0f && speed) {
                info.setReturnValue(1.0f);
                return;
            }

            f = (float) ((double) f * ((iattributeinstance.getAttributeValue() / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0d) + add);

            if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
                f = 1.0F;
            }

            if (this.isHandActive() && this.getActiveItemStack().getItem() instanceof net.minecraft.item.ItemBow) {
                int i = this.getItemInUseMaxCount();
                float f1 = (float) i / 20.0F;

                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 = f1 * f1;
                }

                if (FovModifier.get().aim() == 0.0f) {
                    info.setReturnValue(1.0f);
                    return;
                }

                f *= 1.0F - f1 * 0.15F + FovModifier.get().aim() / ten;
            }

            info.setReturnValue(net.minecraftforge.client.ForgeHooksClient.getOffsetFOV(this, f));
        }
    }
}
