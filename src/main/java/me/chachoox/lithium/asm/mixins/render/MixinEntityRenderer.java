package me.chachoox.lithium.asm.mixins.render;

import com.google.common.base.Predicate;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.events.render.misc.RenderHandEvent;
import me.chachoox.lithium.impl.event.events.screen.AspectRatioEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.antihitbox.AntiHitBox;
import me.chachoox.lithium.impl.modules.render.blockhighlight.BlockHighlight;
import me.chachoox.lithium.impl.modules.render.fullbright.Fullbright;
import me.chachoox.lithium.impl.modules.render.nametags.Nametags;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow
    @Final
    private int[] lightmapColors;

    @Final
    @Shadow
    private Minecraft mc;

    @Redirect(method = "setupCameraTransform", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V", remap = false))
    public void onSetupCameraTransform(final float fovy, final float aspect, final float zNear, final float zFar) {
        final AspectRatioEvent event = new AspectRatioEvent(mc.displayWidth / (float) mc.displayHeight);
        Bus.EVENT_BUS.dispatch(event);
        Project.gluPerspective(fovy, event.getAspectRatio(), zNear, zFar);
    }

    @Redirect(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V", remap = false))
    public void onRenderWorldPass(final float fovy, final float aspect, final float zNear, final float zFar) {
        final AspectRatioEvent event = new AspectRatioEvent(mc.displayWidth / (float) mc.displayHeight);
        Bus.EVENT_BUS.dispatch(event);
        Project.gluPerspective(fovy, event.getAspectRatio(), zNear, zFar);
    }

    @Redirect(method = "renderCloudsCheck", at = @At(value = "INVOKE", target = "Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V", remap = false))
    public void onRenderCloudsCheck(final float fovy, final float aspect, final float zNear, final float zFar) {
        final AspectRatioEvent event = new AspectRatioEvent(mc.displayWidth / (float) mc.displayHeight);
        Bus.EVENT_BUS.dispatch(event);
        Project.gluPerspective(fovy, event.getAspectRatio(), zNear, zFar);
    }

    @Inject(method = "displayItemActivation(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    public void preDisplayItemActivation(ItemStack stack, CallbackInfo callbackInfo) {
        if (stack.getItem() == Items.TOTEM_OF_UNDYING && Managers.MODULE.get(NoRender.class).getTotemAnimation()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderItemActivation(IIF)V", at = @At("HEAD"), cancellable = true)
    public void preRenderItemActivation(int a, int b, float c, CallbackInfo callbackInfo) {
        if (Managers.MODULE.get(NoRender.class).isEnabled() && Managers.MODULE.get(NoRender.class).getTotemAnimation()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager" + ".clear(I)V", ordinal = 1, shift = At.Shift.BEFORE))
    public void renderWorldPassHook(int pass, float partialTicks, long finishTimeNano, CallbackInfo info) {
        if (Display.isActive() || Display.isVisible()) {
            Bus.EVENT_BUS.dispatch(new Render3DEvent(partialTicks));
        }
    }

    @Inject(method = "renderHand", at = @At(value="HEAD"), cancellable = true)
    private void renderHandHook(CallbackInfo info) {
        RenderHandEvent event = new RenderHandEvent();
        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "drawNameplate", at = @At("HEAD"), cancellable = true)
    private static void renderName(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo ci) {
        if (Managers.MODULE.get(Nametags.class).isEnabled()) {
            ci.cancel();
        }
    }

    @Redirect(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;" + "drawSelectionBox(" + "Lnet/minecraft/entity/player/EntityPlayer;" + "Lnet/minecraft/util/math/RayTraceResult;IF)V"))
    public void drawSelectionBoxHook(RenderGlobal renderGlobal, EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
        if (!Managers.MODULE.get(BlockHighlight.class).isEnabled()) {
            renderGlobal.drawSelectionBox(player, movingObjectPositionIn, execute, partialTicks);
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void noHurtcamHook(float entitylivingbase, CallbackInfo ci) {
        if (Managers.MODULE.get(NoRender.class).isEnabled() && Managers.MODULE.get(NoRender.class).getHurtCam()) {
            ci.cancel();
        }
    }

    @SuppressWarnings("Guava")
    @Redirect(method = "getMouseOver", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcludingHook(WorldClient worldClient, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        if (Managers.MODULE.get(AntiHitBox.class).canTrace()) {
            return new ArrayList<>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Inject(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;updateDynamicTexture()V", shift = At.Shift.BEFORE))
    private void updateTextureHook(float partialTicks, CallbackInfo ci) {
        final Fullbright FULL_BRIGHT = Managers.MODULE.get(Fullbright.class);
        if (FULL_BRIGHT.isEnabled() && FULL_BRIGHT.customColor()) {
            for (int i = 0; i < this.lightmapColors.length; ++i) {
                int alpha = FULL_BRIGHT.getColor().getAlpha();
                float modifier = (float) alpha / 255.0f;
                int color = this.lightmapColors[i];
                int[] bgr = toRGBAArray(color);
                Vector3f values = new Vector3f(
                        (float) bgr[2] / 255.0f,
                        (float) bgr[1] / 255.0f,
                        (float) bgr[0] / 255.0f);
                Vector3f newValues = new Vector3f(
                        (float) FULL_BRIGHT.getColor().getRed() / 255.0f,
                        (float) FULL_BRIGHT.getColor().getGreen() / 255.0f,
                        (float) FULL_BRIGHT.getColor().getBlue() / 255.0f);
                Vector3f value = mix(values, newValues, modifier);
                int red = (int) (value.x * 255.0f);
                int green = (int) (value.y * 255.0f);
                int blue = (int) (value.z * 255.0f);
                this.lightmapColors[i] = 0xFF000000 | red << 16 | green << 8 | blue;
            }
        }
    }

    private int[] toRGBAArray(int colorBuffer) {
        return new int[]{colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF};
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    }
}
