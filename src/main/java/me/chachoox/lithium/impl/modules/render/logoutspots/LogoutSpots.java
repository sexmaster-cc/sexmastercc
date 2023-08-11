package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.render.logoutspots.mode.RenderMode;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LogoutSpots extends Module {

    protected final EnumProperty<RenderMode> render =
            new EnumProperty<>(
                    RenderMode.OUTLINE,
                    new String[]{"Render", "ghost", "outline", "mode", "type"},
                    "Outline: - Renders a outlined box with a nametag at the logout spot " +
                            "/ Ghost: - Renders the player's skin and a nametag at the logout spot."
            );

    protected final NumberProperty<Float> scaling =
            new NumberProperty<>(
                    0.3f, 0.1f, 1.0f, 0.1f,
                    new String[]{"Scale", "size"},
                    "The scale of the nametags."
            );

    protected final Property<Boolean> rect =
            new Property<>(
                    true,
                    new String[]{"Rect", "rectangle"},
                    "Draws a rectangle behind the nametag text."
            );

    protected final Property<Boolean> syncBorder =
            new Property<>(
                    false,
                    new String[]{"GlobalOutline", "syncoutline"},
                    "Syncs the outline color."
            );

    protected final Property<Boolean> message =
            new Property<>(
                    false,
                    new String[]{"Message", "msg"},
                    "Sends a message whenever a cached player joins / leaves the server."
            );

    protected final ColorProperty textColor =
            new ColorProperty(new Color(-1), false,
                    new String[]{"TextColor", "infocolor"}
            );

    protected final ColorProperty boxColor =
            new ColorProperty(new Color(255, 255, 255, 255), true,
                    new String[]{"BoxColor", "OutlineColor"}
            );

    protected final ColorProperty ghostColor =
            new ColorProperty(new Color(188, 188, 188, 88), false,
                    new String[]{"GhostColor", "modelcolor"}
            );

    public final Map<UUID, LogoutSpot> spots = new ConcurrentHashMap<>();
    protected StopWatch timer = new StopWatch();

    public LogoutSpots() {
        super("LogoutSpots", new String[]{"LogoutSpots", "logout", "logspots"}, "Shows where players logged out.", Category.RENDER);
        this.offerProperties(render, scaling, rect, syncBorder, message, textColor, boxColor, ghostColor);
        this.offerListeners(new ListenerLogout(this), new ListenerJoin(this), new ListenerLeave(this), new ListenerRender(this));
    }

    @Override
    public void onDisable() {
        spots.clear();
    }

    @Override
    public void onWorldLoad() {
        spots.clear();
        timer.reset();
    }

    public Color getTextColor() {
        return textColor.getColor();
    }

    public Color getBoxColor() {
        return boxColor.getColor();
    }

    public Color getGhostColor() {
        return ghostColor.getColor();
    }

    protected void renderNameTag(String text, AxisAlignedBB interpolated) {
        double x = (interpolated.minX + interpolated.maxX) / 2.0;
        double y = (interpolated.minY + interpolated.maxY) / 2.0;
        double z = (interpolated.minZ + interpolated.maxZ) / 2.0;

        renderNameTag(text, x, (y - 0.2), z);
    }

    protected void renderNameTag(String name, double x, double y, double z) {
        double distance = RenderUtil.getEntity().getDistance(
                x + mc.getRenderManager().viewerPosX,
                y + mc.getRenderManager().viewerPosY,
                z + mc.getRenderManager().viewerPosZ);
        int width = Managers.FONT.getStringWidth(name) >> 1;
        double scale = 0.0018 + MathUtil.fixedNametagScaling(scaling.getValue()) * distance;

        if (distance <= 8) {
            scale = 0.0245D;
        }

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.translate((float) x, (float) y + 1.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        if (rect.getValue()) {
            int borderCol = syncBorder.getValue() ? Colours.get().getColour().getRGB() : (int) (127.0f * 0.88f) << 24;
            Render2DUtil.drawNameTagRect(-width - 2, -(mc.fontRenderer.FONT_HEIGHT + 1), width + 2F, 1.5F, (int) (127.0f * 0.66f) << 24, borderCol, 1.4F);
        }

        Managers.FONT.drawString(name, -width, -(8), getTextColor().getRGB());

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
