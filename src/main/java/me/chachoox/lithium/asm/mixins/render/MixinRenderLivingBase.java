package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.impl.event.events.render.misc.DamageColorEvent;
import me.chachoox.lithium.impl.event.events.render.model.ModelRenderEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import me.chachoox.lithium.impl.modules.render.skeleton.Skeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.FloatBuffer;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase <T extends EntityLivingBase> extends Render<T> {

    @Shadow
    protected ModelBase mainModel;

    @Shadow
    protected FloatBuffer brightnessBuffer;

    @Shadow
    public abstract int getColorMultiplier(EntityLivingBase entityLivingBase, float lightBrightness, float partialTickTime);

    private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Overwrite
    protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
        float f = entitylivingbaseIn.getBrightness();
        int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 255) > 0;
        boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;

        if (!flag && !flag1) {
            return false;
        } else if (!flag && !combineTextures) {
            return false;
        } else {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            this.brightnessBuffer.position(0);

            if (flag1) {
                final DamageColorEvent damageColorEvent;
                Bus.EVENT_BUS.dispatch(damageColorEvent = new DamageColorEvent(1.0F, 0.0F, 0.0F, 0.3F));
                this.brightnessBuffer.put(damageColorEvent.getRed());
                this.brightnessBuffer.put(damageColorEvent.getGreen());
                this.brightnessBuffer.put(damageColorEvent.getBlue());
                this.brightnessBuffer.put(damageColorEvent.getAlpha());
            } else {
                float f1 = (float)(i >> 24 & 255) / 255.0F;
                float f2 = (float)(i >> 16 & 255) / 255.0F;
                float f3 = (float)(i >> 8 & 255) / 255.0F;
                float f4 = (float)(i & 255) / 255.0F;
                this.brightnessBuffer.put(f2);
                this.brightnessBuffer.put(f3);
                this.brightnessBuffer.put(f4);
                this.brightnessBuffer.put(1.0F - f1);
            }

            this.brightnessBuffer.flip();
            GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
            GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }

    //TODO: move all events to (onRender) same with the RenderEnderCrystal mixin
    @Inject(method = "renderModel", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.BEFORE), cancellable = true)
    private void preRenderModel(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci, boolean flag, boolean flag1) {
        RenderLivingBase<?> renderLiving = RenderLivingBase.class.cast(this);
        ModelRenderEvent event = new ModelRenderEvent.Pre(renderLiving, entitylivingbaseIn, renderLiving.getMainModel(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        Bus.EVENT_BUS.dispatch(event);
        if (event.isCanceled()) {
            Bus.EVENT_BUS.dispatch(new ModelRenderEvent.Post(renderLiving, entitylivingbaseIn, renderLiving.getMainModel(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor));
            if (flag1) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            ci.cancel();
        }
    }

    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.AFTER))
    private void postRenderModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        RenderLivingBase<?> renderLiving = RenderLivingBase.class.cast(this);
        Bus.EVENT_BUS.dispatch(new ModelRenderEvent.Post(renderLiving, entity, renderLiving.getMainModel(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor));
    }

    @Inject(method = "renderLayers", at = @At("RETURN"))
    public void renderLayersHook(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn, CallbackInfo ci) {
        final Skeleton SKELETON = Managers.MODULE.get(Skeleton.class);
        if (SKELETON.isEnabled()) {
            SKELETON.onRenderModel(mainModel, entitylivingbaseIn);
        }
    }

    @Inject(method = "doRender", at = @At("HEAD"))
    public void doRenderPre(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        final Chams CHAMS = Managers.MODULE.get(Chams.class);
        if (CHAMS.isEnabled() && CHAMS.normal.getValue()) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    public void doRenderPost(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        final Chams CHAMS = Managers.MODULE.get(Chams.class);
        if (CHAMS.isEnabled() && CHAMS.normal.getValue()) {
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
}
