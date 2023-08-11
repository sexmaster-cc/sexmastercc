package me.chachoox.lithium.impl.modules.render.chams;

import me.chachoox.lithium.impl.event.events.render.model.ModelRenderEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

import static org.lwjgl.opengl.GL11.*;

public class ListenerPreModel extends ModuleListener<Chams, ModelRenderEvent.Pre> {
    public ListenerPreModel(Chams module) {
        super(module, ModelRenderEvent.Pre.class);
    }

    @Override
    public void call(ModelRenderEvent.Pre event) {
        if (module.playerChams.getValue()) {
            if (!module.normal.getValue()) {
                if (event.getEntity() == mc.player && !module.self.getValue()) {
                    return;
                }
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

                    glColor4d(module.getVisibleColor(event.getEntity()).getRed() / 255f,
                            module.getVisibleColor(event.getEntity()).getGreen() / 255f,
                            module.getVisibleColor(event.getEntity()).getBlue() / 255f,
                            module.getVisibleColor(event.getEntity()).getAlpha() / 255f);

                    event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());

                    glDepthMask(true);
                    glEnable(GL_DEPTH_TEST);
                }

                glColor4d(module.getInvisibleColor(event.getEntity()).getRed() / 255f,
                        module.getInvisibleColor(event.getEntity()).getGreen() / 255f,
                        module.getInvisibleColor(event.getEntity()).getBlue() / 255f,
                        module.getInvisibleColor(event.getEntity()).getAlpha() / 255f);

                event.getModel().render(event.getEntity(), event.getLimbSwing(), event.getLimbSwingAmount(), event.getAgeInTicks(), event.getNetHeadYaw(), event.getHeadPitch(), event.getScale());

                glEnable(GL_TEXTURE_2D);
                glEnable(GL_LIGHTING);
                glDisable(GL_BLEND);
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
}
