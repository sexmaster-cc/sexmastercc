package me.chachoox.lithium.impl.gui.click.item.properties;

import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;

import java.awt.*;

public class BooleanButton extends Button {

    private final Property<Boolean> booleanProperty;

    public BooleanButton(Property<Boolean> property) {
        super(property.getLabel());
        this.booleanProperty = property;
        this.x = getX() + 1f;
        setProperty(booleanProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(x - 1.0f, y, x, y + height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : 290805077);
        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }
        renderer.drawString(!ClickGUI.get().lowercaseProperties.getValue() ? getLabel() + ClickGUI.get().aliProperty() : getLabel().toLowerCase() + ClickGUI.get().aliProperty(), x + 2.3f, y + 4.0f, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        booleanProperty.setValue(!booleanProperty.getValue());
    }

    @Override
    public boolean getState() {
        return booleanProperty.getValue();
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

