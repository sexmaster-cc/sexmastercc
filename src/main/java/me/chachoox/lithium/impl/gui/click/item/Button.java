package me.chachoox.lithium.impl.gui.click.item;

import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class Button extends Item implements Labeled, Minecraftable {
    private boolean state;

    public Button(String label) {
        super(label);
        height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(x, y, x + width, y + height, getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : ClickGUI.get().getDisabledButtonColor().getRGB());
        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                Render2DUtil.drawRect(x, y, x + width, y + height, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(x, y, x + width, y + height, Colours.get().getColourCustomAlpha(30).getRGB());
            }
        }
        renderer.drawString(
                !ClickGUI.get().lowercaseModules.getValue() ? getLabel() + ClickGUI.get().aliModule() : getLabel().toLowerCase() + ClickGUI.get().aliModule(),
                x + 2.0f,
                y + 4.0f,
                getState() ? ClickGUI.get().getEnabledTextColor().getRGB() : ClickGUI.get().getDisabledTextColor().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            state = !state;
            toggle();
        }
    }

    public void toggle() {
    }

    public boolean getState() {
        return state;
    }

    @Override
    public float getHeight() {
        return 14;
    }
}
