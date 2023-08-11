package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import static org.lwjgl.opengl.GL11.*;

public class ListenerRender extends ModuleListener<LogoutSpots, Render3DEvent> {
    public ListenerRender(LogoutSpots module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (!module.isNull()) {
            for (LogoutSpot spot : module.spots.values()) {

                EntityPlayer player = spot.getEntity();
                AxisAlignedBB bb = Interpolation.interpolateAxis(spot.getBoundingBox());

                switch (module.render.getValue()) {
                    case GHOST: {
                        if (player != null) {
                            player.hurtTime = 0;
                            double x = spot.getX() - mc.getRenderManager().viewerPosX;
                            double y = spot.getY() - mc.getRenderManager().viewerPosY;
                            double z = spot.getZ() - mc.getRenderManager().viewerPosZ;

                            GlStateManager.pushMatrix();
                            glEnable(GL_POLYGON_OFFSET_FILL);
                            glPolygonOffset(1.0f, -1100000.0f);
                            GlStateManager.color(module.getGhostColor().getRed() / 255F, module.getGhostColor().getGreen() / 255F, module.getGhostColor().getBlue() / 255F, module.getGhostColor().getAlpha() / 255F);
                            mc.getRenderManager().renderEntity(player, x, y, z, player.rotationYaw, 0, false);
                            glDisable(GL_POLYGON_OFFSET_FILL);
                            glPolygonOffset(1.0f, 1100000.0f);
                            GlStateManager.popMatrix();

                            player.setPosition(
                                    Interpolation.interpolateLastTickPos(player.lastTickPosX, player.posX),
                                    Interpolation.interpolateLastTickPos(player.lastTickPosY, player.posY),
                                    Interpolation.interpolateLastTickPos(player.lastTickPosZ, player.posZ));
                        }
                        break;
                    }
                    case OUTLINE: {
                        RenderUtil.startRender();
                        RenderUtil.drawOutline(bb, 1.5f, module.getBoxColor());
                        RenderUtil.endRender();
                        break;
                    }

                }

                String text = spot.getName()
                        + " logged out at "
                        + MathUtil.round(spot.getX(), 2)
                        + ", "
                        + MathUtil.round(spot.getY(), 2)
                        + ", "
                        + MathUtil.round(spot.getZ(), 2);

                module.renderNameTag(text, bb);
            }
        }

    }
}
