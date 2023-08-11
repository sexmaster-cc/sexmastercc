package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.render.betterchat.BetterChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat extends Gui {

    @Shadow
    public List<ChatLine> drawnChatLines;

    @Shadow
    public int scrollPos;

    @Shadow
    public Minecraft mc;

    @Shadow
    public boolean isScrolled;

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();

    @ModifyConstant(method = "setChatLine", constant = @Constant(intValue = 100))
    public int setChatLineHook(int line) {
        return Managers.MODULE.get(BetterChat.class).isInfinite() ? Integer.MAX_VALUE : line;
    }

    @ModifyVariable(method = "setChatLine", at = @At(value="HEAD"), ordinal = 0)
    private int setChatLineHook2(int line) {
        if (line == Logger.PERMANENT_ID) {
            line = 0;
        }

        return line;
    }

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiNewChat.setChatLine(Lnet/minecraft/util/text/ITextComponent;IIZ)V", shift = At.Shift.AFTER), cancellable = true)
    private void printChatHook(ITextComponent component, int line, CallbackInfo callbackInfo) {
        if (line == Logger.MESSAGE_ID || line == Logger.CUSTOM_ID || line == Logger.PERMANENT_ID) {
            callbackInfo.cancel();
        }
    }

    @Overwrite
    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (j > 0) {
                boolean flag = false;

                if (this.getChatOpen()) {
                    flag = true;
                }

                float f1 = this.getChatScale();
                int k = MathHelper.ceil((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 8.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                final BetterChat BETTER_CHAT = Managers.MODULE.get(BetterChat.class);
                final float rectAlpha = BETTER_CHAT.isEnabled() ? BETTER_CHAT.getRectAlpha() : 1.0f;
                if (rectAlpha > 0.0f) {
                    int height = 0;
                    for (int i3 = 0; i3 + this.scrollPos < this.drawnChatLines.size() && i3 < i; ++i3) {
                        ChatLine chatLine = this.drawnChatLines.get(i3 + this.scrollPos);
                        if (chatLine != null) {
                            double denormalizeClamp = MathHelper.clamp((1.0 - (updateCounter - chatLine.getUpdatedCounter()) / 200.0) * 10.0, 0.0, 1.0);
                            if ((255.0 * (denormalizeClamp * denormalizeClamp)) >= 255 || flag) {
                                height -= this.mc.fontRenderer.FONT_HEIGHT;
                            }
                        }
                    }
                    Gui.drawRect(-2, 0, k + 4, height, (int)(127.0f * rectAlpha) << 24);
                }

                int l = 0;
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                    if (chatline != null) {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double)j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int)(255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int)((float)l1 * f);
                            ++l;

                            if (l1 > 3) {
                                int j2 = -i1 * 9;
                                if (rectAlpha > 0.0f && l1 < 255) {
                                    drawRect(-2, j2 - 9, k + 4, j2, (int)(rectAlpha * (l1 / 2)) << 24);
                                }
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();

                                final String name = mc.player.getName();

                                if (BETTER_CHAT.isEnabled()) {
                                    final StringBuilder sb = new StringBuilder();
                                    for (int i4 = 0; i4 < s.length(); ++i4) {
                                        if (s.regionMatches(true, i4, name, 0, name.length())) {
                                            sb.append((BETTER_CHAT.getPlayerColor())).append(s, i4, i4 + name.length()).append(TextUtil.findMatch(s.substring(0, i4 + name.length())));
                                            i4 += name.length() - 1;
                                        } else {
                                            sb.append(s.charAt(i4));
                                        }
                                    }

                                    s = sb.toString();
                                }

                                if (BETTER_CHAT.noAnnoyingPeople()) {
                                    s = s.toLowerCase();
                                }

                                final CustomFont CUSTOM_FONT = Managers.MODULE.get(CustomFont.class);
                                if (CUSTOM_FONT.isChat() && !CUSTOM_FONT.isFull()) {
                                    Managers.FONT.fontRenderer.drawStringWithShadow(s, 0.0F, (float) (j2 - 8), 16777215 + (l1 << 24));
                                } else {
                                    this.mc.fontRenderer.drawStringWithShadow(s, 0.0F, (float) (j2 - 8), 16777215 + (l1 << 24));
                                }

                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag) {
                    int k2 = this.mc.fontRenderer.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

}

