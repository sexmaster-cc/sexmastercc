package me.chachoox.lithium.impl.gui.click;

import me.chachoox.lithium.api.interfaces.Labeled;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.item.Button;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;

public abstract class Panel implements Labeled, Minecraftable {
    private final String label;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private final int width;
    private final int height;
    private boolean open;
    public boolean drag;
    private final ArrayList<Item> items;

    public Panel(String label, int x, int y, boolean open) {
        items = new ArrayList<>();
        this.label = label;
        this.x = x;
        this.y = y;
        width = 100;
        height = 18;
        this.open = open;
        setupItems();
    }

    public abstract void setupItems();

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drag(mouseX, mouseY);
        float totalItemHeight = open ? (getTotalItemHeight() - 2.0f) : 0.0f;
        Render2DUtil.drawRect(x, y - 1.5f, (x + width), (y + height - 6), ClickGUI.get().getCategoryColor().getRGB());
        if (open) {
            Render2DUtil.drawRect(x, y + 12.0f, (x + width), y + height + totalItemHeight - 1.5F, ClickGUI.get().getBackgroundColor().getRGB());
        }
        Managers.FONT.drawString(!ClickGUI.get().lowercaseCategories.getValue() ? getLabel() + ClickGUI.get().aliCategory() : getLabel().toLowerCase() + ClickGUI.get().aliCategory(), x + 3.0f, y + 2.0f, ClickGUI.get().getCategoryTextColor().getRGB());

        if (open) {
            float y = getY() + getHeight() - 3.0f;
            for (Item item : getItems()) {
                item.setLocation(x + 2.0f, y - 1.0F);
                item.setWidth(getWidth() - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                y += item.getHeight() + 2.0f;
            }
        }
    }

    private void drag(int mouseX, int mouseY) {
        if (!drag) {
            return;
        }
        x = x2 + mouseX;
        y = y2 + mouseY;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
            x2 = x - mouseX;
            y2 = y - mouseY;
            SexMasterGui.getClickGui().getPanels().forEach(panel -> {
                if (panel.drag) {
                    panel.drag = false;
                }
            });
            drag = true;
            return;
        }
        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void addButton(Button button) {
        items.add(button);
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            drag = false;
        }
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getOpen() {
        return open;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() - 1 && mouseY >= getY() - 1.5F && mouseY <= getY() + getHeight() - 6;
    }

    private float getTotalItemHeight() {
        float height = 0.0f;
        for (Item item : getItems()) {
            height += item.getHeight() + 2.0f;
        }
        return height;
    }

    public void setX(int dragX) {
        x = dragX;
    }

    public void setY(int dragY) {
        y = dragY;
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!open) {
            return;
        }
        getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
}

