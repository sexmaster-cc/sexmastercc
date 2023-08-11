package me.chachoox.lithium.impl.gui.click.item;

import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.managers.client.FontManager;
import me.chachoox.lithium.impl.managers.Managers;

public class Item implements Labeled {
    private final String label;
    private Property<?> property;
    protected float x;
    protected float y;
    protected float width;
    protected float height;

    protected FontManager renderer = Managers.FONT;

    public Item(String label) {
        this.label = label;
    }

    public Item(String label, Property<?> property) {
        this.label = label;
        this.property = property;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
    }

    public void onKeyTyped(char typedChar, int keyCode) {
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Property<?> getProperty() {
        return this.property;
    }

    public void setProperty(Property<?> property) {
        this.property = property;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Panel panel : SexMasterGui.getClickGui().getPanels()) {
            if (!panel.drag) continue;
            return false;
        }
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + height;
    }
}

