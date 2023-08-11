package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.thread.events.IClickEvent;
import me.chachoox.lithium.asm.ducks.IStyle;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.inventorypreview.InventoryPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen extends Gui implements GuiYesNoCallback {

    @Shadow
    @Final
    private static Set<String> PROTOCOLS;

    @Shadow
    public Minecraft mc;

    @Shadow
    private URI clickedLinkURI;

    @Shadow
    protected abstract void openWebLink(URI url);

    @Shadow
    protected abstract void setText(String newChatText, boolean shouldOverwrite);

    @Shadow
    public abstract void sendChatMessage(String msg, boolean addToChat);

    @Shadow
    public static boolean isShiftKeyDown() {
        throw new IllegalStateException("isShiftKeyDown was not shadowed!");
    }

    @Shadow
    RenderItem itemRender;

    @Shadow
    protected FontRenderer fontRenderer;

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        if (Managers.MODULE.get(InventoryPreview.class).isShulker() && stack.getItem() instanceof ItemShulkerBox) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
                if (blockEntityTag.hasKey("Items", 9)) {
                    info.cancel();

                    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);

                    GlStateManager.enableBlend();
                    GlStateManager.disableRescaleNormal();
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();

                    int width = Math.max(144, fontRenderer.getStringWidth(stack.getDisplayName())+3);

                    int x1 = x + 12;
                    int y1 = y - 12;
                    int height = 48+9;

                    this.itemRender.zLevel = 300.0F;
                    this.drawGradientRectP(x1 - 3, y1 - 4, x1 + width + 3, y1 - 3, -267386864, -267386864);
                    this.drawGradientRectP(x1 - 3, y1 + height + 3, x1 + width + 3, y1 + height + 4, -267386864, -267386864);
                    this.drawGradientRectP(x1 - 3, y1 - 3, x1 + width + 3, y1 + height + 3, -267386864, -267386864);
                    this.drawGradientRectP(x1 - 4, y1 - 3, x1 - 3, y1 + height + 3, -267386864, -267386864);
                    this.drawGradientRectP(x1 + width + 3, y1 - 3, x1 + width + 4, y1 + height + 3, -267386864, -267386864);
                    this.drawGradientRectP(x1 - 3, y1 - 3 + 1, x1 - 3 + 1, y1 + height + 3 - 1, 1347420415, 1344798847);
                    this.drawGradientRectP(x1 + width + 2, y1 - 3 + 1, x1 + width + 3, y1 + height + 3 - 1, 1347420415, 1344798847);
                    this.drawGradientRectP(x1 - 3, y1 - 3, x1 + width + 3, y1 - 3 + 1, 1347420415, 1347420415);
                    this.drawGradientRectP(x1 - 3, y1 + height + 2, x1 + width + 3, y1 + height + 3, 1344798847, 1344798847);

                    fontRenderer.drawString(stack.getDisplayName(), x+12, y-12, 0xffffff);

                    GlStateManager.enableBlend();
                    GlStateManager.enableAlpha();
                    GlStateManager.enableTexture2D();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();
                    for (int i = 0; i < nonnulllist.size(); i++) {
                        int iX = x + (i % 9) * 16 + 11;
                        int iY = y + (i / 9) * 16 - 11 + 8;
                        ItemStack itemStack = nonnulllist.get(i);

                        itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                        itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack, iX, iY, null);
                    }
                    RenderHelper.disableStandardItemLighting();
                    this.itemRender.zLevel = 0.0F;

                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();
                }
            }
        }
    }

    private void drawGradientRectP(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 300).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 300).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 300).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Inject(method = "handleComponentClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;" + "sendChatMessage(Ljava/lang/String;Z)V", shift = At.Shift.BEFORE), cancellable = true)
    public void handleComponentClick(ITextComponent component, CallbackInfoReturnable<Boolean> info) {
        IClickEvent event = (IClickEvent) component.getStyle().getClickEvent();
        if (event != null && event.getRunnable() != null) {
            event.getRunnable().run();
            info.setReturnValue(true);
        }
    }

    protected boolean handleClick(ITextComponent component, int button) {
        if (component == null) {
            return false;
        }

        IStyle style = (IStyle) component.getStyle();
        ClickEvent event = null;

        if (button == 1) {
            event = style.getRightClickEvent();
        } else if (button == 2) {
            event = style.getMiddleClickEvent();
        }

        if (isShiftKeyDown()) {
            String insertion = null;
            if (button == 1) {
                insertion = style.getRightInsertion();
            } else if (button == 2) {
                insertion = style.getMiddleInsertion();
            }

            if (insertion != null) {
                this.setText(insertion, false);
            }
        } else if (event != null) {
            if (event.getAction() == ClickEvent.Action.OPEN_URL) {
                if (!this.mc.gameSettings.chatLinks) {
                    return false;
                } try {
                    URI uri = new URI(event.getValue());
                    String s = uri.getScheme();

                    if (s == null) {
                        throw new URISyntaxException(event.getValue(), "Missing protocol");
                    }

                    if (!PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                        throw new URISyntaxException(event.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                    }
                    if (this.mc.gameSettings.chatLinksPrompt) {
                        this.clickedLinkURI = uri;
                        this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, event.getValue(), 31102009, false));
                    } else {
                        this.openWebLink(uri);
                    }
                } catch (URISyntaxException urisyntaxexception) {
                    Logger.getLogger().log(Level.ERROR, "Can't open url for " + event + " : " + urisyntaxexception);
                }
            } else if (event.getAction() == ClickEvent.Action.OPEN_FILE) {
                URI uri1 = (new File(event.getValue())).toURI();
                this.openWebLink(uri1);
            } else if (event.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                this.setText(event.getValue(), true);
            } else if (event.getAction() == ClickEvent.Action.RUN_COMMAND) {
                if (((IClickEvent) event).getRunnable() != null) {
                    ((IClickEvent) event).getRunnable().run();
                    return true;
                }

                this.sendChatMessage(event.getValue(), false);
            } else {
                Logger.getLogger().log(Level.ERROR, "Don't know how to handle " + event);
            }

            return true;
        }

        return false;
    }

}