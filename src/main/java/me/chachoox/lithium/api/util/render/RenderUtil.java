package me.chachoox.lithium.api.util.render;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil implements Minecraftable {

    public static Entity getEntity() {
        return mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity();
    }

    public static void startRender() {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL11.GL_FASTEST);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public static void endRender() {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void drawBox(AxisAlignedBB bb) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        fillBox(bb);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBox(AxisAlignedBB bb, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        color(color);
        fillBox(bb);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawOutline(AxisAlignedBB bb, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        fillOutline(bb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawCross(AxisAlignedBB bb, float lineWidth, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        color(color);
        fillCross(bb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void fillCross(AxisAlignedBB bb) {
        if (bb != null) {
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            }
            GL11.glEnd();
        }
    }

    public static void drawOutline(AxisAlignedBB bb, float lineWidth, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        color(color);
        fillOutline(bb);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    public static void fillOutline(AxisAlignedBB bb) {
        if (bb != null) {
            GL11.glBegin(GL11.GL_LINES);
            {
                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
                GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
            }
            GL11.glEnd();
        }
    }

    public static void fillBox(AxisAlignedBB boundingBox) {
        if (boundingBox != null) {
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glEnd();

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
            GL11.glVertex3d((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glVertex3d((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
            GL11.glEnd();
        }
    }

    public static void color(Color color) {
        GL11.glColor4f(color.getRed() / 255.0f,
                color.getGreen() / 255.0f,
                color.getBlue() / 255.0f,
                color.getAlpha() / 255.0f
        );
    }

    public static void color(int color) {
        float[] color4f = ColorUtil.toArray(color);
        GL11.glColor4f(color4f[0], color4f[1], color4f[2], color4f[3]);
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }
}

