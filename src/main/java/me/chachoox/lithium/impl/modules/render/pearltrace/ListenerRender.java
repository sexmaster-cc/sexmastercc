package me.chachoox.lithium.impl.modules.render.pearltrace;

import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

public class ListenerRender extends ModuleListener<PearlTrace, Render3DEvent> {
    public ListenerRender(PearlTrace module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        double
                x
                =
                Interpolation
                        .
                        getRenderPosX
                                ();
        double
                y
                =
                Interpolation
                        .getRenderPosY
                                ();
        double
                z
                =
                Interpolation
                        .
                        getRenderPosZ
                                ();
        RenderUtil
                .
                startRender
                        ();
        GL11
                .glLineWidth
                        (
                                module.width.getValue()
                        );
        GL11.glColor4f(module.getColor().getRed() / 255.0f, module.getColor().getGreen() / 255.0f, module.getColor().getBlue() / 255.0f, module.getColor().getAlpha() / 255.0f);
        module.thrownEntities.forEach((id, thrownEntity) ->
        {
            GL11.glBegin(GL_LINE_STRIP);
            for (Vec3d vertex : thrownEntity.getVertices()) {
                Vec3d vec = vertex.subtract(x, y, z);
                GL11.glVertex3d(vec.x, vec.y, vec.z);
            }
            GL11.glEnd();
        });
        RenderUtil.endRender();
    }
}
