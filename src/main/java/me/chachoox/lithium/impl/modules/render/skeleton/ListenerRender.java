package me.chachoox.lithium.impl.modules.render.skeleton;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class ListenerRender extends ModuleListener<Skeleton, Render3DEvent> {
    public ListenerRender(Skeleton module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        boolean lightning = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean lineSmooth = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);

        if (lightning) {
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        if (!blend) {
            GL11.glEnable(GL11.GL_BLEND);
        }

        GL11.glLineWidth(module.getLineWidth());

        if (texture) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }

        if (depth) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        if (!lineSmooth) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        }

        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        GlStateManager.depthMask(false);

        Entity renderEntity = RenderUtil.getEntity();
        Frustum frustum = Interpolation.createFrustum(renderEntity);
        List<EntityPlayer> playerList = mc.world.playerEntities;
        module.rotationList.keySet().removeIf(player -> player == null
                || player.equals(renderEntity)
                || player.isPlayerSleeping()
                || player.isSpectator()
                || EntityUtil.isDead(player));
        playerList.forEach(player -> {
            AxisAlignedBB bb = player.getEntityBoundingBox();
            if (!frustum.isBoundingBoxInFrustum(bb)) {
                return;
            }

            if (module.rotationList.get(player) != null) {
                renderSkeleton(player, module.rotationList.get(player));
            }
        });

        GlStateManager.depthMask(true);

        if (!lineSmooth) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        if (depth) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (texture) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (!blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        if (lightning) {
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    protected void renderSkeleton(EntityPlayer player, float[][] rotations) {
        GlStateManager.pushMatrix();

        if (Managers.FRIEND.isFriend(player)) {
            final Color frdColor = Colours.get().getFriendColour();
            GlStateManager.color(frdColor.getRed() / 255.0f, frdColor.getGreen() / 255.0f, frdColor.getBlue() / 255.0f, frdColor.getAlpha() / 255.0f);
        } else {
            final Color color = module.getColor();
            GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        }

        Vec3d interpolateEntity = Interpolation.interpolateEntity(player);
        GlStateManager.translate(interpolateEntity.x, interpolateEntity.y , interpolateEntity.z);
        GlStateManager.rotate(-player.renderYawOffset, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? -0.235D : 0.0D);
        float sneak = player.isSneaking() ? 0.6F : 0.75F;
        if (player.isElytraFlying()) {
            float f = (float) player.getTicksElytraFlying() + mc.getRenderPartialTicks();
            float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
            GlStateManager.rotate(f1 * (90.0F - player.rotationPitch), 1.0F, 0.0F, 0.0F);
            Vec3d vec3d = player.getLook(mc.getRenderPartialTicks());
            double d0 = player.motionX * player.motionX + player.motionZ * player.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (player.motionX * vec3d.x + player.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = player.motionX * vec3d.z - player.motionZ * vec3d.x;
                GlStateManager.rotate((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.125D, sneak, 0.0D);
        if (rotations[3][0] != 0.0F) {
            GlStateManager.rotate(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        if (rotations[3][1] != 0.0F) {
            GlStateManager.rotate(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if (rotations[3][2] != 0.0F) {
            GlStateManager.rotate(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, -sneak);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.125D, sneak, 0.0D);
        if (rotations[4][0] != 0.0F) {
            GlStateManager.rotate(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        if (rotations[4][1] != 0.0F) {
            GlStateManager.rotate(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if (rotations[4][2] != 0.0F) {
            GlStateManager.rotate(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, (-sneak));
        GlStateManager.glEnd();

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? 0.25D : 0.0D);
        GlStateManager.pushMatrix();
        double sneakOffset = 0.0;
        if (player.isSneaking()) {
            sneakOffset = -0.05;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.01725D : 0.0D);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.375D, sneak + 0.55D, 0.0D);
        if (rotations[1][0] != 0.0F) {
            GlStateManager.rotate(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        if (rotations[1][1] != 0.0F) {
            GlStateManager.rotate(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if (rotations[1][2] != 0.0F) {
            GlStateManager.rotate(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, -0.5D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.375D, sneak + 0.55D, 0.0D);
        if (rotations[2][0] != 0.0F) {
            GlStateManager.rotate(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }
        if (rotations[2][1] != 0.0F) {
            GlStateManager.rotate(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
        }
        if (rotations[2][2] != 0.0F) {
            GlStateManager.rotate(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, -0.5D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, sneak + 0.55D, 0.0D);
        if (rotations[0][0] != 0.0F) {
            GlStateManager.rotate(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
        }

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, 0.3D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
        GlStateManager.rotate(player.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);

        if (player.isSneaking()) {
            sneakOffset = -0.16175D;
        }

        GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.48025D : 0.0D);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, sneak, 0.0D);

        GlStateManager.glBegin(3);
        GL11.glVertex2d(-0.125D, 0.0D);
        GL11.glVertex2d(0.125D, 0.0D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, sneak, 0.0D);

        GlStateManager.glBegin(3);
        GL11.glVertex2d(0.0D, 0.0D);
        GL11.glVertex2d(0.0D, 0.55D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, sneak + 0.55D, 0.0D);

        GlStateManager.glBegin(3);
        GL11.glVertex2d(-0.375D, 0.0D);
        GL11.glVertex2d(0.375D, 0.0D);
        GlStateManager.glEnd();

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
