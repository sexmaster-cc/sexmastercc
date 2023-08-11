package me.chachoox.lithium.impl.modules.render.esp;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.ColorProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.esp.util.RenderMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class ESP extends Module {

    protected final EnumProperty<RenderMode> mode =
            new EnumProperty<>(
                    RenderMode.OUTLINE,
                    new String[]{"Mode", "type"},
                    "Outline: - Draws an outline / Box: Draws a box."
            );

    protected final Property<Boolean> items =
            new Property<>(
                    true,
                    new String[]{"Items", "i"},
                    "Draws esp on items."
            );

    protected final Property<Boolean> xpBottles =
            new Property<>(
                    true,
                    new String[]{"Bottles", "expbottles"},
                    "Draws esp on xp bottles."
            );

    protected final Property<Boolean> pearls =
            new Property<>(
                    false,
                    new String[]{"Pearls", "pearl", "p"},
                    "Draws esp on ender pearls."
            );

    protected final NumberProperty<Float> lineWidth =
            new NumberProperty<>(
                    1f, 1f, 4f, 0.1f,
                    new String[]{"WireWidth", "width", "linewidth"},
                    "Thickness of the line."
            );

    protected final ColorProperty color =
            new ColorProperty(
                    new Color(-1),
                    true,
                    new String[]{"Color", "colour"}
            );

    protected final NumberProperty<Integer> boxAlpha =
            new NumberProperty<>(
                    85, 0, 255,
                    new String[]{"BoxAlpha", "alpha", "b"},
                    "Alpha of the box."
            );

    protected final Property<Boolean> itemNametags =
            new Property<>(
                    false,
                    new String[]{"ItemNametags", "tags", "itemsname"},
                    "Draws nametags on dropped items with the item count and the name of the item."
            );

    protected final Property<Boolean> chorus =
            new Property<>(
                    false,
                    new String[]{"ChorusFruit", "Chorus", "Teleports"},
                    "Draws text on where chorus fruit teleport sounds spawn."
            );

    protected final NumberProperty<Float> scaling =
            new NumberProperty<>(
                    0.3f, 0.1f, 1.0f, 0.1f,
                    new String[]{"Scaling", "scale"},
                    "Size of the item nametags."
            );

    protected final ColorProperty nametagColor =
            new ColorProperty(
                    new Color(-1),
                    false,
                    new String[]{"NametagColor", "nametagcolor", "tagcolor"}
            );

    protected BlockPos teleportPos;
    protected final StopWatch teleportTimer = new StopWatch();

    public ESP() {
        super("ESP", new String[]{"ESP", "ItemEsp", "EntityEsp"}, "Draws boxes and outlines on entites.", Category.RENDER);
        this.offerListeners(new ListenerRender(this), new ListenerSound(this));
        this.offerProperties(mode, itemNametags, chorus, xpBottles, items, pearls, lineWidth, color, boxAlpha, scaling, nametagColor);
    }

    protected Color getColor() {
        return color.getColor();
    }

    protected Color getNametagColor() {
        return nametagColor.getColor();
    }

    protected Color getBoxColor() {
        return ColorUtil.changeAlpha(color.getColor(), boxAlpha.getValue());
    }

    protected boolean isValid(Entity entity) {
        boolean valid = false;
        if (entity instanceof EntityItem && items.getValue()) {
            valid = true;
        } else if (entity instanceof EntityExpBottle && xpBottles.getValue()) {
            valid = true;
        } else if (entity instanceof EntityEnderPearl && pearls.getValue()) {
            valid = true;
        }
        return valid;
    }

    protected void doRender(AxisAlignedBB bb) {
        RenderUtil.startRender();
        switch (mode.getValue()) {
            case BOX:
                renderBox(bb);
                break;
            case OUTLINE:
                renderOutline(bb);
                break;
            case BOTH:
                renderBox(bb);
                renderOutline(bb);
                break;
        }
        RenderUtil.endRender();
    }

    //(entity.ticksExisted <= 42 ? (entity.ticksExisted * 6) / 255.0f) : 1.0)); implement

    protected void renderBox(AxisAlignedBB bb) {
        RenderUtil.drawBox(bb, getBoxColor());
    }

    public void renderOutline(AxisAlignedBB bb) {
        RenderUtil.drawOutline(bb, lineWidth.getValue(), getColor());
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
        GlStateManager.translate((float) x, (float) y + 0.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        Managers.FONT.drawString(name, -width, -(8), getNametagColor().getRGB());

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
