package me.chachoox.lithium.impl.modules.render.nametags;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.norender.NoRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;

public class ListenerRender extends ModuleListener<Nametags, Render3DEvent> {
    public ListenerRender(Nametags module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        final Entity renderEntity = RenderUtil.getEntity();
        final Frustum frustum = Interpolation.createFrustum(renderEntity);
        final Vec3d interp = Interpolation.interpolateEntity(renderEntity);
        final List<EntityPlayer> playerList = mc.world.playerEntities;
        playerList.sort(Comparator.comparing(player -> mc.player.getDistance((EntityPlayer) player)).reversed());
        for (EntityPlayer player : playerList) {
            AxisAlignedBB bb = player.getEntityBoundingBox();
            Vec3d vec = Interpolation.interpolateEntity(player);

            if (!frustum.isBoundingBoxInFrustum(bb.expand(0.75, 0.75, 0.75))
                    || player == renderEntity
                    || EntityUtil.isDead(player)
                    || player.isInvisible() && !module.invisibles.getValue()
                    || Managers.MODULE.get(NoRender.class).getNoSpectators() && player.isSpectator()) {
                continue;
            }

            renderNameTag(player, vec.x, vec.y, vec.z, interp);
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, Vec3d mcPlayerInterpolation) {
        double tempY = y + (player.isSneaking() ? 0.5D : 0.7D);
        double xDist = mcPlayerInterpolation.x - x;
        double yDist = mcPlayerInterpolation.y - y;
        double zDist = mcPlayerInterpolation.z - z;
        y = MathHelper.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);

        final String displayTag = module.getDisplayTag(player);
        final int width = Managers.FONT.getStringWidth(displayTag) / 2;
        double scale = 0.0018 + MathUtil.fixedNametagScaling(module.scaling.getValue()) * y;

        if (y <= 8) {
            scale = 0.0245D;
        }

        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();

        GlStateManager.translate((float) x, (float) tempY + 1.4F, (float) z);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        float xRot = mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
        GlStateManager.rotate(mc.getRenderManager().playerViewX, xRot, 0.0f, 0.0f);

        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();

        if (module.rectBorder.getValue()) {
            Render2DUtil.drawNameTagRect(-width - 2,
                    -(mc.fontRenderer.FONT_HEIGHT + 1),
                    width + 2F,
                    1.5F,
                    (int) (127.0f * module.opacity.getValue()) << 24,
                    module.holeColor.getValue()
                            ? (EntityUtil.isPlayerSafe(player)
                            ? 0xFF14C800 : 0xFFC80014)
                            : module.getBorderColor(player), 1.4F);
        }

        Managers.FONT.drawString(displayTag, -width, -8, module.getNameColor(player));

        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();

        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        ItemStack heldItemOffhand = player.getHeldItemOffhand();

        int xOffset = 0;
        int enchantOffset = 0;

        int i;
        int armorSize = i = 3;

        while (i >= 0) {
            final ItemStack itemStack;
            if (!(itemStack = player.inventory.armorInventory.get(armorSize)).isEmpty()) {
                xOffset -= 8;
                final int size;
                if ((module.enchants.getValue() && !module.simple.getValue()) && (size = EnchantmentHelper.getEnchantments(itemStack).size()) > enchantOffset) {
                    enchantOffset = size;
                }
            }
            i = --armorSize;
        }

        if ((!heldItemOffhand.isEmpty() && (module.armor.getValue()) || (module.durability.getValue() && heldItemOffhand.isItemStackDamageable()))) {
            xOffset -= 8;
            final int size2;
            if ((module.enchants.getValue() && !module.simple.getValue()) && (size2 = EnchantmentHelper.getEnchantments(heldItemOffhand).size()) > enchantOffset) {
                enchantOffset = size2;
            }
        }

        if (!heldItemMainhand.isEmpty()) {
            final int size3;
            if ((module.enchants.getValue() && !module.simple.getValue()) && (size3 = EnchantmentHelper.getEnchantments(heldItemMainhand).size()) > enchantOffset) {
                enchantOffset = size3;
            }

            int armorOffset = getOffset(enchantOffset);
            if (module.armor.getValue() || (module.durability.getValue() && heldItemMainhand.isItemStackDamageable())) {
                xOffset -= 8;
            }

            if (module.armor.getValue()) {
                final int oldOffset = armorOffset;
                armorOffset -= 32;
                module.renderStack(heldItemMainhand, xOffset, oldOffset, enchantOffset);
            }

            if (module.durability.getValue() && heldItemMainhand.isItemStackDamageable()) {
                module.renderDurability(heldItemMainhand, xOffset, armorOffset);
            }

            if (module.itemStack.getValue()) {
                module.renderText(heldItemMainhand, (armorOffset - (module.durability.getValue() ? 10 : 2)));
            }

            if (module.armor.getValue() || (module.durability.getValue() && heldItemMainhand.isItemStackDamageable())) {
                xOffset += 16;
            }
        }

        int i2;
        int armorSizeI = i2 = 3;

        while (i2 >= 0) {
            final ItemStack itemStack3;

            if (!(itemStack3 = player.inventory.armorInventory.get(armorSizeI)).isEmpty()) {

                int fixedEnchantOffset = getOffset(enchantOffset);

                if (module.armor.getValue()) {
                    final int oldEnchantOffset = fixedEnchantOffset;
                    fixedEnchantOffset -= 32;
                    module.renderStack(itemStack3, xOffset, oldEnchantOffset, enchantOffset);
                }

                if (module.durability.getValue() && itemStack3.isItemStackDamageable()) {
                    module.renderDurability(itemStack3, xOffset, fixedEnchantOffset);
                }

                xOffset += 16;
            }

            i2 = --armorSizeI;
        }

        if (!heldItemOffhand.isEmpty()) {

            int fixedEnchantOffsetI = getOffset(enchantOffset);

            if (module.armor.getValue()) {
                final int oldEnchantOffsetI = fixedEnchantOffsetI;
                fixedEnchantOffsetI -= 32;
                module.renderStack(heldItemOffhand, xOffset, oldEnchantOffsetI, enchantOffset);
            }

            if (module.durability.getValue() && heldItemOffhand.isItemStackDamageable()) {
                module.renderDurability(heldItemOffhand, xOffset, fixedEnchantOffsetI);
            }

        }

        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private int getOffset(int offset) {
        int fixedOffset = module.armor.getValue() ? -26 : -27;
        if (offset > 4) {
            fixedOffset -= (offset - 4) * 8;
        }
        return fixedOffset;
    }
}