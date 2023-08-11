package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.impl.event.events.render.model.CrystalModelRenderEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.chams.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderEnderCrystal.class)
public abstract class MixinRenderEnderCrystal extends Render<EntityEnderCrystal> implements Minecraftable {

    @Shadow
    @Final
    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");

    @Shadow
    @Final
    private ModelBase modelEnderCrystal;

    @Shadow
    @Final
    private ModelBase modelEnderCrystalNoBase;

    protected MixinRenderEnderCrystal(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(method = "doRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        final Chams CHAMS = Managers.MODULE.get(Chams.class);
        if (CHAMS.isEnabled() && CHAMS.normal.getValue()) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }

        CrystalModelRenderEvent event = new CrystalModelRenderEvent.Pre(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Bus.EVENT_BUS.dispatch(event);

        if (event.isCanceled()) {
            Bus.EVENT_BUS.dispatch(new CrystalModelRenderEvent.Post(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale));
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Overwrite
    public void doRender(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        final Chams CHAMS = Managers.MODULE.get(Chams.class);
        float defaultSpinSpeed = entity.innerRotation + partialTicks;
        float spinTicks =  entity.innerRotation + partialTicks;
        float bounceSpeed = MathHelper.sin(defaultSpinSpeed * 0.2f * CHAMS.bounceSpeed.getValue()) / 2.0f + 0.5f;
        float scale = CHAMS.scale.getValue();
        float spinSpeed = CHAMS.spinSpeed.getValue();
        bounceSpeed = bounceSpeed * bounceSpeed + bounceSpeed;

        ModelBase model = entity.shouldShowBottom() ? this.modelEnderCrystal : this.modelEnderCrystalNoBase;

        GlStateManager.pushMatrix();
        GlStateManager.translate( x,  y,  z);
        bindTexture(ENDER_CRYSTAL_TEXTURES);
        float spinFactor = MathHelper.sin(defaultSpinSpeed * 0.2F) / 2.0F + 0.5F;
        spinFactor += spinFactor * spinFactor;

        //TODO: find a way to implement this
        /*
        CrystalModelRenderEvent event = new CrystalModelRenderEvent.Post(model, entity, 0.0F, defaultSpinSpeed * 3.0F, spinFactor * 0.2F, 0.0F, 0.0F, 0.0625F);
        Bus.EVENT_BUS.dispatch(event);
        */

        if (CHAMS.isEnabled()) {
            if (!CHAMS.normal.getValue()) {
                GlStateManager.scale(scale, scale, scale);
                model.render(entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);

                if (CHAMS.crystalWires.getValue()) {
                    CHAMS.onWireframeModel(model, entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);
                }

                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            } else {
                GlStateManager.scale(scale, scale, scale);
                model.render(entity, 0.0f, spinTicks * 3.0f * spinSpeed, bounceSpeed * 0.2f, 0.0f, 0.0f, 0.0625f);
                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            }
        } else {
            model.render(entity, 0.0F, defaultSpinSpeed * 3.0F, spinFactor * 0.2F, 0.0F, 0.0F, 0.0625F);
        }

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        BlockPos blockpos = entity.getBeamTarget();

        if (blockpos != null) {
            bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float f2 = blockpos.getX() + 0.5F;
            float f3 = blockpos.getY() + 0.5F;
            float f4 = blockpos.getZ() + 0.5F;
            double d0 = f2 - entity.posX;
            double d1 = f3 - entity.posY;
            double d2 = f4 - entity.posZ;
            RenderDragon.renderCrystalBeams(x + d0, y - 0.3D + (spinFactor * 0.4F) + d1, z + d2, partialTicks, f2, f3, f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
        }
    }
}
