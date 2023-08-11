package me.chachoox.lithium.impl.gui.click.item.properties;

import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.awt.*;

public class EnumButton extends Button {

    private final EnumProperty<?> property;

    public EnumButton(EnumProperty<?> property) {
        super(property.getLabel());
        this.property = property;
        this.x = getX() + 1f;
        setProperty(property);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Render2DUtil.drawRect(x - 1.0f, y, x, y + height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : ClickGUI.get().getDisabledButtonColor().getRGB());
        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }
        renderer.drawString(String.format("%s:%s %s",
                !ClickGUI.get().lowercaseProperties.getValue() ? getLabel() + ClickGUI.get().aliProperty() : getLabel().toLowerCase() + ClickGUI.get().aliProperty(),
                ClickGUI.get().whiteResult.getValue() ? TextColor.WHITE : TextColor.GRAY,
                !ClickGUI.get().lowercaseProperties.getValue() ? (property.getFixedValue()) : property.getFixedValue().toLowerCase()),
                x + 2.3F, y + 4.0F, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                property.increment();
            } else if (mouseButton == 1) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                property.decrement();
            }
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean getState() {
        return true;
    }

    @Override
    public boolean isHovering(int mouseX, int mouseY) {
        for (Panel panel : SexMasterGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return mouseX >= getX() && mouseX <= getX() + (width + 6.9F) && mouseY >= getY() && mouseY <= getY() + height;
    }
}