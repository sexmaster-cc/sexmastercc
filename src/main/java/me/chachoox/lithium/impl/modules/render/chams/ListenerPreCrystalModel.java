package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.model.CrystalModelRenderEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class ListenerPreCrystalModel extends ModuleListener<Chams, CrystalModelRenderEvent.Pre> {
    public ListenerPreCrystalModel(Chams module) {
        super(module, CrystalModelRenderEvent.Pre.class);
    }

    @Override
    public void call(CrystalModelRenderEvent.Pre event) {
        if (module.crystalChams.getValue() && !module.normal.getValue()) {

            glPushMatrix();

            if (module.texture.getValue()) {
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }

            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glDisable(GL_ALPHA_TEST);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_STENCIL_TEST);
            glEnable(GL_POLYGON_OFFSET_LINE);

            if (module.xqz.getValue()) {
                glDepthMask(false);
                glDisable(GL_DEPTH_TEST);

                GL11.glColor4d(module.getInvisibleColor(event.getEntity()).getRed() / 255f, module.getInvisibleColor(event.getEntity()).getGreen() / 255f, module.getInvisibleColor(event.getEntity()).getBlue() / 255f, module.getInvisibleColor(event.getEntity()).getAlpha() / 255f);
                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());

                glDepthMask(true);
                glEnable(GL_DEPTH_TEST);
            }

            glColor4d(module.getVisibleColor(event.getEntity()).getRed() / 255f, module.getVisibleColor(event.getEntity()).getGreen() / 255f, module.getVisibleColor(event.getEntity()).getBlue() / 255f, module.getVisibleColor(event.getEntity()).getAlpha() / 255f);
            event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());

            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glPopAttrib();
            glPopMatrix();

            if (module.glint.getValue()) {
                module.onGlintModel(event.getModel(), event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());
            }

            event.setCanceled(true);
        }
    }
}
