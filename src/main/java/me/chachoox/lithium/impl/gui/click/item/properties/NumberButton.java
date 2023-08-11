package me.chachoox.lithium.impl.gui.click.item.properties;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.math.RoundingUtil;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.gui.click.SexMasterGui;
import me.chachoox.lithium.impl.gui.click.Panel;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class NumberButton extends Item implements Minecraftable {
    private final NumberProperty<Number> numberProperty;
    private final Number min;
    private final Number max;
    private final int difference;

    private boolean dragging;

    public NumberButton(NumberProperty<Number> numberProperty) {
        super(numberProperty.getLabel());
        this.numberProperty = numberProperty;
        this.min = numberProperty.getMinimum();
        this.max = numberProperty.getMaximum();
        this.difference = max.intValue() - min.intValue();
        setProperty(numberProperty);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRect(x - 1.0f, y, x, y + height - 0.5f, ClickGUI.get().getPropertyColor().getRGB());
        Render2DUtil.drawRect(x, y, numberProperty.getValue().floatValue() <= min.floatValue() ? x : x + (width + 6.9F) * partialMultiplier(), y + height - 0.5f, ClickGUI.get().getEnabledButtonColor().getRGB());
        if (isHovering(mouseX, mouseY)) {
            Render2DUtil.drawRect(x, y, x + (width + 6.9F) * partialMultiplier(), y + height - 0.5f, ColorUtil.changeAlpha(Color.BLACK, 30).getRGB());
        }
        if (dragging) {
            setSettingFromX(mouseX);
        }

        String value = String.format("%.2f", numberProperty.getValue().floatValue());
        renderer.drawString(String.format("%s:%s %s",
                        !ClickGUI.get().lowercaseProperties.getValue() ? getLabel() + ClickGUI.get().aliProperty() : getLabel().toLowerCase() + ClickGUI.get().aliProperty(),
                        ClickGUI.get().whiteResult.getValue() ? TextColor.WHITE : TextColor.GRAY,
                        value),
                x + 2.3F,
                y + 4.0F,
                0xFFFFFFFF);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovering(mouseX, mouseY) && mouseButton == 0) {
            dragging = true;
        }
    }

    private void setSettingFromX(int mouseX) {
        float percent = (mouseX - x) / (width + 6.9F);
        if (numberProperty.getValue() instanceof Double) {
            double result = (Double)numberProperty.getMinimum() + (difference * percent);
            numberProperty.setValue(MathHelper.clamp(RoundingUtil.roundDouble(RoundingUtil.roundToStep(result, (double) numberProperty.getSteps()), 2), (double) numberProperty.getMinimum(), (double) numberProperty.getMaximum()));
        } else if (numberProperty.getValue() instanceof Float) {
            float result = (Float)numberProperty.getMinimum() + (difference * percent);
            numberProperty.setValue(MathHelper.clamp(RoundingUtil.roundFloat(RoundingUtil.roundToStep(result, (float) numberProperty.getSteps()), 2), (float) numberProperty.getMinimum(), (float) numberProperty.getMaximum()));
        } else if (numberProperty.getValue() instanceof Integer) {
            numberProperty.setValue(((Integer)numberProperty.getMinimum() + (int)(difference * percent)));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    @Override
    public float getHeight() {
        return 14;
    }

    private float middle() {
        return max.floatValue() - min.floatValue();
    }

    private float part() {
        return numberProperty.getValue().floatValue() - min.floatValue();
    }

    private float partialMultiplier() {
        return part() / middle();
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

