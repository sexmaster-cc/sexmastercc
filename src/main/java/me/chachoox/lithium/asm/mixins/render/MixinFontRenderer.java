package me.chachoox.lithium.asm.mixins.render;

import me.chachoox.lithium.api.util.colors.HSLColor;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.regex.Pattern;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {

    private static final String COLOR_CODES = "0123456789abcdefklmnorzy+-=";

    private static final Pattern CUSTOM_PATTERN = Pattern.compile("(?i)\u00a7Z[0-9A-F]{8}");

    @Shadow
    private boolean randomStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean strikethroughStyle;
    @Shadow
    private int textColor;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;
    @Shadow
    private float alpha;

    private int skip;
    private int currentIndex;
    private boolean currentShadow;
    private String currentText;
    private boolean rainbowPlus;
    private boolean rainbowMinus;
    private boolean rainbowExtra;

    @Shadow
    protected abstract int renderString(String text, float x, float y, int color, boolean dropShadow);

    @Shadow
    protected abstract void renderStringAtPos(String string, boolean shadow);

    @Shadow
    protected abstract int getCharWidth(char character);

    @Redirect(method = "renderString(Ljava/lang/String;FFIZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V"))
    public void renderStringAtPosHook(FontRenderer renderer, String text, boolean shadow) {
        final NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        final EntityPlayer player = Minecraft.getMinecraft().player;

        if (NAME_PROTECT != null && NAME_PROTECT.isEnabled() && player != null) {
            renderStringAtPos(text.replace(player.getName(), NAME_PROTECT.getName()), shadow);
        } else {
            renderStringAtPos(text, shadow);
        }
    }

    @Inject(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "HEAD"), cancellable = true)
    public void renderStringHook2(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> infoReturnable) {
        if (Managers.MODULE.get(CustomFont.class) != null && Managers.MODULE.get(CustomFont.class).isFull()) {
            if (dropShadow) {
                Managers.FONT.fontRenderer.drawStringWithShadow(text, x, y, color);
            } else {
                Managers.FONT.fontRenderer.drawString(text, x, y, color);
            }
            infoReturnable.setReturnValue((int) (Managers.FONT.getStringWidth(text) + x));
        }
    }

    @Overwrite
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            final NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
            final EntityPlayer player = Minecraft.getMinecraft().player;
            if (NAME_PROTECT != null && NAME_PROTECT.isEnabled() && player != null) {
                text = text.replace(player.getName(), NAME_PROTECT.getName());
            }

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                int k = this.getCharWidth(c0);

                if (k < 0 && j < text.length() - 1) {
                    ++j;
                    c0 = text.charAt(j);

                    if (c0 != 'l' && c0 != 'L') {
                        if (c0 == 'r' || c0 == 'R') {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }

            CustomFont CUSTOM_FONT = Managers.MODULE.get(CustomFont.class);
            if (CUSTOM_FONT != null && CUSTOM_FONT.isFull()) {
                i = Managers.FONT.fontRenderer.getStringWidth(text);
            }

            return i;
        }
    }

    @Redirect(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;" + "renderString(Ljava/lang/String;FFIZ)I"))
    public int renderStringHook(FontRenderer fontrenderer, String text, float x, float y, int color, boolean dropShadow) {
        if (dropShadow && !Managers.MODULE.get(Hud.class).shadow.getValue()) {
            x -= 0.4f;
            y -= 0.4f;
        }

        return this.renderString(text, x, y, color, dropShadow);
    }

    @Inject(method = "renderStringAtPos", at = @At(value = "HEAD"))
    public void resetSkip(String text, boolean shadow, CallbackInfo info) {
        skip = 0;
        currentIndex = 0;
        currentText = text;
        currentShadow = shadow;
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 0))
    public char charAtHook(String text, int index) {
        currentIndex = index;
        return getCharAt(text, index);
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    public char charAtHook1(String text, int index) {
        return getCharAt(text, index);
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", ordinal = 0))
    public int lengthHook(String string) {
        return string.length() - skip;
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", ordinal = 1))
    public int lengthHook1(String string) {
        return string.length() - skip;
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0))
    public int colorCodeHook(String colorCode, int ch) {
        int result = COLOR_CODES.indexOf(String.valueOf(currentText.charAt(currentIndex + skip + 1)).toLowerCase().charAt(0));

        if (result == 22) {
            this.randomStyle        = false;
            this.boldStyle          = false;
            this.strikethroughStyle = false;
            this.underlineStyle     = false;
            this.italicStyle        = false;
            this.rainbowPlus        = false;
            this.rainbowMinus       = false;
            this.rainbowExtra       = false;

            char[] h = new char[8];

            try {
                for (int j = 0; j < 8; j++) {
                    h[j] = currentText.charAt(currentIndex + skip + j + 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }

            int colorcode = 0xffffffff;

            try {
                colorcode = (int) Long.parseLong(new String(h), 16);
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.textColor = colorcode;
            GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (colorcode >> 8 & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (colorcode & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (colorcode >> 24 & 0xFF) / 255.0f);
            skip += 8;
        } else if (result == 23) {
            this.randomStyle        = false;
            this.boldStyle          = false;
            this.strikethroughStyle = false;
            this.underlineStyle     = false;
            this.italicStyle        = false;
            this.rainbowPlus        = false;
            this.rainbowMinus       = false;
            this.rainbowExtra       = false;

            int rainbow = HSLColor.toRGB(Colours.get().getRainbowHue(), Colours.get().saturation.getValue(), Colours.get().lightness.getValue()).getRGB();
            GlStateManager.color((rainbow >> 16 & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (rainbow >> 8 & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (rainbow & 0xFF) / 255.0f / (currentShadow ? 4 : 1), (rainbow >> 24 & 0xFF) / 255.0f);
        } else if (result == 24) {
            this.randomStyle        = false;
            this.boldStyle          = false;
            this.strikethroughStyle = false;
            this.underlineStyle     = false;
            this.italicStyle        = false;
            this.rainbowPlus        = true;
            this.rainbowMinus       = false;
            this.rainbowExtra       = false;
        } else if (result == 25) {
            this.randomStyle        = false;
            this.boldStyle          = false;
            this.strikethroughStyle = false;
            this.underlineStyle     = false;
            this.italicStyle        = false;
            this.rainbowPlus        = false;
            this.rainbowMinus       = true;
            this.rainbowExtra       = false;
        } else if (result == 26) {
            this.randomStyle        = false;
            this.boldStyle          = false;
            this.strikethroughStyle = false;
            this.underlineStyle     = false;
            this.italicStyle        = false;
            this.rainbowPlus        = false;
            this.rainbowMinus       = false;
            this.rainbowExtra       = true;
        } else {
            this.rainbowPlus  = false;
            this.rainbowMinus = false;
            this.rainbowExtra = false;
        }

        return result;
    }

    @Inject(method = "resetStyles", at = @At("HEAD"))
    public void resetStylesHook(CallbackInfo info) {
        this.rainbowPlus  = false;
        this.rainbowMinus = false;
        this.rainbowExtra = false;
    }

    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderChar(CZ)F", shift = At.Shift.BEFORE, ordinal = 0))
    public void renderCharHook(String text, boolean shadow, CallbackInfo info) {
        if (this.rainbowPlus || this.rainbowMinus || this.rainbowExtra) {
            int rainbow = HSLColor.toRGB(Colours.get().getRainbowHueByPosition(getRainbowPos(this.rainbowPlus, this.rainbowMinus, this.rainbowExtra) * Colours.get().factor.getValue()), Colours.get().saturation.getValue(), Colours.get().lightness.getValue()).getRGB();
            GlStateManager.color((rainbow >> 16 & 0xFF) / 255.0f / (shadow ? 4 : 1), (rainbow >> 8 & 0xFF) / 255.0f / (shadow ? 4 : 1), (rainbow & 0xFF) / 255.0f / (shadow ? 4 : 1), this.alpha);
        }
    }

    @ModifyVariable(method = "getStringWidth", at = @At(value="HEAD"), ordinal = 0)
    private String setText(String text) {
        return text == null ? null : CUSTOM_PATTERN.matcher(text).replaceAll(TextColor.AQUA);
    }

    private char getCharAt(String text, int index) {
        if (index + skip >= text.length()) {
            return text.charAt(text.length() - 1);
        }

        return text.charAt(index + skip);
    }

    //china but works so idk
    private float getRainbowPos(boolean plus, boolean minus, boolean extra) {
        if (plus) {
            return this.posX;
        } else if (minus) {
            return this.posY;
        } else if (extra) {
            return (this.posX + this.posY);
        }
        return 0f;
    }

}