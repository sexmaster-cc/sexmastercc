package me.chachoox.lithium.impl.modules.player.fakelag;

import me.chachoox.lithium.asm.mixins.render.IRenderManager;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;

import static org.lwjgl.opengl.GL11.*;

public class ListenerRender extends ModuleListener<FakeLag, Render3DEvent> {
    public ListenerRender(FakeLag module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if ((mc.player.posX != mc.player.prevPosX || mc.player.posY != mc.player.prevPosY || mc.player.posZ != mc.player.prevPosZ)) {
            module.positons.add(new Vector3d(mc.player.posX, mc.player.posY, mc.player.posZ));
        }

        GL11.glPushMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_DEPTH_TEST);
        GL11.glLineWidth(1.5F);

        GL11.glBegin(GL_LINE_STRIP);

        GL11.glColor4f(
                module.lineColor.getValue().getRed() / 255f,
                module.lineColor.getValue().getGreen() / 255f,
                module.lineColor.getValue().getBlue() / 255f,
                module.lineColor.getValue().getAlpha() / 255f);

        module.positons.forEach(vector ->
                GL11.glVertex3d(
                        vector.x - ((IRenderManager) mc.getRenderManager()).getRenderPosX(),
                        vector.y - ((IRenderManager) mc.getRenderManager()).getRenderPosY(),
                        vector.z - ((IRenderManager) mc.getRenderManager()).getRenderPosZ()));

        GL11.glEnd();
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glDisable(GL_BLEND);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
