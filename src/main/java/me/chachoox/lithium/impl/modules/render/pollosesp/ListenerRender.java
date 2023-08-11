package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pollosesp.util.VectorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

//TODO: WHY THIS DOESNT WORK
public class ListenerRender extends ModuleListener<PollosESP, Render3DEvent> {
    public ListenerRender(PollosESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != RenderUtil.getEntity() && !EntityUtil.isDead(player)) {
                Vec3d bottomVec = Interpolation.interpolateEntity(player);
                Vec3d topVec = bottomVec.add(new Vec3d(0, (player.getRenderBoundingBox().maxY - player.posY), 0));
                VectorUtil.ScreenPos top = VectorUtil.toScreenie(topVec.x, topVec.y, topVec.z);
                VectorUtil.ScreenPos bot = VectorUtil.toScreenie(bottomVec.x, bottomVec.y, bottomVec.z);
                if (top.isVisible || bot.isVisible) {
                    int height = (bot.y - top.y);
                    int width = height;

                    int x = (int) (top.x - (width / 1.8));
                    int y = top.y;

                    mc.renderEngine.bindTexture(PollosESP.POLLOS);
                    GlStateManager.color(255.0f, 255.0f, 255.0f);
                    Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, width, height);
                }
            }
        }
    }

}
