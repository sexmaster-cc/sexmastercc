package me.chachoox.lithium.impl.modules.render.tracers;

import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.asm.mixins.render.IEntityRenderer;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ListenerRender extends ModuleListener<Tracers, Render3DEvent> {
    public ListenerRender(Tracers module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity.posY < module.ydistance.getValue() && entity != mc.player) {

                Vec3d interpolation = Interpolation.interpolateEntity(entity);
                double x = interpolation.x;
                double y = interpolation.y;
                double z = interpolation.z;

                RenderUtil.startRender();
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();

                if (Managers.FRIEND.isFriend(entity.getName())) {
                    GL11.glColor4f(0.33333334f, 0.78431374f, 0.78431374f, 0.55f);
                } else {
                    float distance = RenderUtil.getEntity().getDistance(entity);
                    float red;
                    if (distance >= 60.0f) {
                        red = 120.0f;
                    }
                    else {
                        red = distance + distance;
                    }// Toby harnack
                    Color color = ColorUtil.toColor(red, 100.0f, 50.0f, module.opacity.getValue());
                    GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
                }

                final boolean viewBobbing = mc.gameSettings.viewBobbing;
                mc.gameSettings.viewBobbing = false;
                ((IEntityRenderer) mc.entityRenderer).invokeOrientCamera(event.getPartialTicks());
                mc.gameSettings.viewBobbing = viewBobbing;
                GL11.glLineWidth(1.0F);
                final Vec3d rotateYaw = new Vec3d(0.0, 0.0, 1.0)
                                .rotatePitch(-(float) Math.toRadians(RenderUtil.getEntity().rotationPitch))
                                .rotateYaw(-(float) Math.toRadians(RenderUtil.getEntity().rotationYaw));
                GL11.glBegin(GL11.GL_LINES);

                GL11.glVertex3d(rotateYaw.x, RenderUtil.getEntity().getEyeHeight() + rotateYaw.y, rotateYaw.z);
                switch (module.bone.getValue()) {
                    case HEAD:
                        GL11.glVertex3d(x, y + entity.height - 0.18f, z);
                        break;
                    case CHEST:
                        GL11.glVertex3d(x, y + entity.height / 2.0f, z);
                        break;
                    case DICK:
                        GL11.glVertex3d(x, y + entity.height - 1.1f, z);
                        break;
                    case FEET:
                        GL11.glVertex3d(x, y, z);
                        break;
                }
                GL11.glEnd();
                GL11.glTranslated(x, y, z);
                GL11.glTranslated(-x, -y, -z);
                GlStateManager.popMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                RenderUtil.endRender();
            }
        }
    }
}
