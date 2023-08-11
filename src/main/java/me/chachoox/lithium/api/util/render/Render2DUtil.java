package me.chachoox.lithium.api.util.render;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class Render2DUtil implements Minecraftable {

    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void drawNameTagRect(float left, float top, float right, float bottom, int color, int border, float width) {
        quickDrawRect(left, top, right, bottom, color, false);
        GL11.glLineWidth(width);
        quickDrawRect(left, top, right, bottom, border, true);
    }

    public static void quickDrawRect(final float x, final float y, final float x2, final float y2, final int color, boolean line) {
        final float a = (color >> 24 & 0xFF) / 255F;
        final float r = (color >> 16 & 0xFF) / 255F;
        final float g = (color >> 8 & 0xFF) / 255F;
        final float b = (color & 0xFF) / 255F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(line ? GL11.GL_LINE_LOOP : GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y2, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(x2, y2, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(x2, y, 0.0D).color(r, g, b, a).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(Rectangle rectangle, int color) {
        drawRect(rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, color);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x, float y, float x1, float y1) {
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos(x, y1, 0.0D).endVertex();
        builder.pos(x1, y1, 0.0D).endVertex();
        builder.pos(x1, y, 0.0D).endVertex();
        builder.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, int color) {
        drawRect(x + lineWidth, y, x - lineWidth, y + lineWidth, color);
        drawRect(x + lineWidth, y, width - lineWidth, y + lineWidth, color);
        drawRect(x, y, x + lineWidth, height, color);
        drawRect(width - lineWidth, y, width, height, color);
        drawRect(x + lineWidth, height - lineWidth, width - lineWidth, height, color);
    }

    public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
        enableGL2D();

        x *= 2;
        x1 *= 2;
        y *= 2;
        y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y, y1 - 1, borderC);
        drawVLine(x1 - 1, y, y1, borderC);
        drawHLine(x, x1 - 1, y, borderC);
        drawHLine(x, x1 - 2, y1 - 1, borderC);
        drawRect(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);

        disableGL2D();
    }

    public static void disableGL2D(boolean ignored) {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }

    public static void enableGL2D(boolean ignored) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    public static void drawRect(float x, float y, float x1, float y1, int color, int ignored) {
        enableGL2D(false);
        glColor(color);
        glBegin(GL_QUADS);
        glVertex2f(x, y1);
        glVertex2f(x1, y1);
        glVertex2f(x1, y);
        glVertex2f(x, y);
        glEnd();
        disableGL2D(false);
    }

    public static void enableGL2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void disableGL2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, boolean sideways, int topColor, int bottomColor) {
        float alpha = (topColor >> 24 & 255) / 255.0F;
        float red = (topColor >> 16 & 255) / 255.0F;
        float green = (topColor >> 8 & 255) / 255.0F;
        float blue = (topColor & 255) / 255.0F;

        float alpha2 = (bottomColor >> 24 & 255) / 255.0F;
        float red2 = (bottomColor >> 16 & 255) / 255.0F;
        float green2 = (bottomColor >> 8 & 255) / 255.0F;
        float blue2 = (bottomColor & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        final BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        if (sideways) {
            builder.pos(left, top, 0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos(left, bottom, 0.0D).color(red, green, blue, alpha).endVertex();

            builder.pos(right, bottom, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(right, top, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        } else {
            builder.pos(right, top, 0.0D).color(red, green, blue, alpha).endVertex();
            builder.pos(left, top, 0.0D).color(red, green, blue, alpha).endVertex();

            builder.pos(left, bottom, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(right, bottom, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        }

        tessellator.draw();

        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }

        drawRect(x, x1, y + 1, x1 + 1, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }

        drawRect(x, y + 1, x + 1, x1, y1);
    }


    public static void glColor(int hex) {
        glColor4f((hex >> 24 & 255) / 255.0F, (hex >> 8 & 255) / 255.0F, (hex & 255) / 255.0F, (hex >> 24 & 255) / 255.0F);
    }
}
