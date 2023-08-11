package me.chachoox.lithium.impl.gui.click.item.properties;

import me.chachoox.lithium.api.property.util.Bind;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.text.DotUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;

import java.awt.*;

public class BindButton extends Button {

    private boolean listening;

    private final BindProperty bindProperty;

    public BindButton(BindProperty property) {
        super(property.getLabel());
        this.bindProperty = property;
        this.x = getX() + 1f;
        setProperty(bindProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(x - 1.0f, y, x, y + height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, getState() ? ClickGUI.get().getEnabledButtonColor().getRGB() : ClickGUI.get().getDisabledButtonColor().getRGB());
        if (isHovering(mouseX, mouseY)) {
            if (getState()) {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
            } else {
                Render2DUtil.drawRect(x, y, x + width + 6.9f, y + height - 0.5f, ColorUtil.changeAlpha(Color.WHITE, 30).getRGB());
            }
        }
        String string = listening ? DotUtil.getDots()
                : !ClickGUI.get().aliMode.getValue()
                ? !ClickGUI.get().lowercaseKeybinds.getValue()
                ? bindProperty.getValue().toString().toUpperCase()
                : bindProperty.getValue().toString().toLowerCase()
                : ClickGUI.get().aliKeybind();
        renderer.drawString(String.format("%s:%s %s",
                !ClickGUI.get().lowercaseProperties.getValue() ? getLabel() : getLabel().toLowerCase() + ClickGUI.get().aliProperty(),
                ClickGUI.get().whiteResult.getValue() ? TextColor.WHITE : TextColor.GRAY,
                string), x + 2.3F, y + 4.0F, 0xFFFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (listening) {
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Escape")) {
                return;
            }
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            bindProperty.setValue(bind);
            toggle();
        }
    }

    @Override
    public float getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        listening = !listening;
    }

    @Override
    public boolean getState() {
        return !listening;
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
