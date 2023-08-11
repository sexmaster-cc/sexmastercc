package me.chachoox.lithium.impl.managers.client;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.text.font.CFontRenderer;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.font.CustomFont;
import me.chachoox.lithium.impl.modules.misc.nameprotect.NameProtect;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class FontManager implements Minecraftable {

    public CFontRenderer fontRenderer = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true);

    public boolean cFont;

    public void drawString(String text, float x, float y, int color) {
        if (cFont) {
            fontRenderer.drawStringWithShadow(text, x, y, color);
        } else {
            mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public void setFontRenderer() {
        final CustomFont CUSTOM_FONT = Managers.MODULE.get(CustomFont.class);
        fontRenderer = new CFontRenderer(CUSTOM_FONT.getFont(), CUSTOM_FONT.antiAlias.getValue(), CUSTOM_FONT.fractionalMetrics.getValue());
    }

    public int getStringWidth(String text) {
        final NameProtect NAME_PROTECT = Managers.MODULE.get(NameProtect.class);
        if (NAME_PROTECT.isEnabled() && mc.player != null) {
            text = StringUtils.replace(text, mc.player.getName(), NAME_PROTECT.getName());
        }

        if (cFont) {
            return fontRenderer.getStringWidth(text);
        } else {
            return mc.fontRenderer.getStringWidth(text);
        }
    }

}