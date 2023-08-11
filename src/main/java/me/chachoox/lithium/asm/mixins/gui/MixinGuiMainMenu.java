package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    private long initTime;

    @Shadow
    protected abstract void renderSkybox(int mouseX, int mouseY, float partialTicks);

    @Inject(method = "initGui", at = @At(value = "RETURN"), cancellable = true)
    public void initGuiHook(CallbackInfo info) {
        initTime = System.currentTimeMillis();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawScreenHook0(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        String text = "Logged in as " + mc.getSession().getUsername();
        int textWidth = mc.fontRenderer.getStringWidth(text);
        int j = this.height / 4 + 48;
        mc.fontRenderer.drawString(String.format("%s%s", Managers.MODULE.get(DisplayTweaks.class).getAccountTextColor(), text),
                (width / 2F) - (textWidth / 2F) - 2, j + 72 + (Managers.MODULE.get(DisplayTweaks.class).getAccountMessagePosY()), -1, true);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V"))
    private void drawScreenHook1(GuiMainMenu guiMainMenu, int mouseX, int mouseY, float partialTicks) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            renderSkybox(mouseX, mouseY, partialTicks);
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 0))
    private void drawScreenHook2(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 1))
    private void drawScreenHook3(GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor) {
        if (!Managers.MODULE.get(DisplayTweaks.class).isCustomTitle()) {
            drawGradientRect(left, top, right, bottom, startColor, endColor);
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "HEAD"), cancellable = true)
    public void drawScreenShader(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        final DisplayTweaks DISPLAY_TWEAKS = Managers.MODULE.get(DisplayTweaks.class);
        if (DISPLAY_TWEAKS.isShader() && DISPLAY_TWEAKS.GLSL_SHADER != null) {
            GlStateManager.disableCull();
            DISPLAY_TWEAKS.GLSL_SHADER.useShader(this.width * 2, this.height * 2, mouseX * 2, mouseY * 2, (System.currentTimeMillis() - initTime) / 1000f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(-1f, -1f);
            GL11.glVertex2f(-1f, 1f);
            GL11.glVertex2f(1f, 1f);
            GL11.glVertex2f(1f, -1f);
            GL11.glEnd();
            GL20.glUseProgram(0);
        } else if (DISPLAY_TWEAKS.isImage()) {
            mc.getTextureManager().bindTexture(new ResourceLocation(DISPLAY_TWEAKS.getImage()));
            drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
        } else if (DISPLAY_TWEAKS.isColor()) {
            Render2DUtil.drawGradientRect(-1, -1, width, height, false, DISPLAY_TWEAKS.getTitleColorOne().getRGB(), DISPLAY_TWEAKS.getTitleColorTwo().getRGB());
        }
    }
}
