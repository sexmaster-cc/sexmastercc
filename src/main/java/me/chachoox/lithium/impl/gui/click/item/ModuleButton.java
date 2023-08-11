package me.chachoox.lithium.impl.gui.click.item;

import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.BindProperty;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.item.properties.BindButton;
import me.chachoox.lithium.impl.gui.click.item.properties.BooleanButton;
import me.chachoox.lithium.impl.gui.click.item.properties.EnumButton;
import me.chachoox.lithium.impl.gui.click.item.properties.NumberButton;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Button implements Minecraftable {
    private final Module module;
    private final List<Item> items = new ArrayList<>();
    private boolean subOpen;

    @SuppressWarnings("unchecked")
    public ModuleButton(Module module) {
        super(module.getLabel());
        this.module = module;
        if (!module.getProperties().isEmpty()) {
            for (Property<?> properties : module.getProperties()) {
                if (properties.getValue() instanceof Boolean && !properties.getLabel().equalsIgnoreCase("Enabled") && !properties.getLabel().equalsIgnoreCase("Drawn")) {
                    items.add(new BooleanButton((Property<Boolean>) properties));
                }
                if (properties instanceof EnumProperty) {
                    items.add(new EnumButton((EnumProperty<?>) properties));
                }
                if (properties instanceof NumberProperty) {
                    items.add(new NumberButton((NumberProperty<Number>) properties));
                }
                if (properties instanceof BindProperty && !properties.getLabel().equalsIgnoreCase("Keybind")) {
                    items.add(new BindButton((BindProperty) properties));
                }
            }
        }
        if (!module.getCategory().getLabel().equalsIgnoreCase("Other")) {
            items.add(new BindButton((BindProperty) module.getProperty("Keybind")));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final ScaledResolution resolution = new ScaledResolution(mc);
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (isHovering(mouseX, mouseY) && module.getDescription() != null) {
            Render2DUtil.drawBorderedRect(
                    -1,
                    resolution.getScaledHeight() - 12,
                    renderer.getStringWidth(!ClickGUI.get().lowercaseDescriptions.getValue() ? module.getDescription() + 9 : module.getDescription().toLowerCase() + 9),
                    resolution.getScaledHeight() + mc.fontRenderer.FONT_HEIGHT + 10,
                    ClickGUI.get().getDescriptionColor().getRGB(),
                    ClickGUI.get().getDescriptionOutlineColor().getRGB());
            renderer.drawString(
                    !ClickGUI.get().lowercaseDescriptions.getValue() ? module.getDescription() : module.getDescription().toLowerCase(),
                    2,
                    resolution.getScaledHeight() - 10,
                    ClickGUI.get().getDescriptionTextColor().getRGB());
        }
        GlStateManager.translate(0.0f, 0.0f, -1.0f);

        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!items.isEmpty()) {
            if (subOpen) {
                float height = 1.0f;
                for (Item item : items) {
                    item.setLocation(x + 1.0f, y + (height += 15.5f));
                    item.setHeight(15);
                    item.setWidth(width - 9);

                    GlStateManager.translate(0.0f, 0.0f, 1.0f);
                    if (item.isHovering(mouseX, mouseY) && !StringUtils.isNullOrEmpty(item.getProperty().getDescription())) {
                        Render2DUtil.drawBorderedRect(
                                -1,
                                resolution.getScaledHeight() - 12,
                                renderer.getStringWidth(!ClickGUI.get().lowercaseDescriptions.getValue() ? item.getProperty().getDescription() + 9 : item.getProperty().getDescription().toLowerCase() + 9),
                                resolution.getScaledHeight() + mc.fontRenderer.FONT_HEIGHT + 10,
                                ClickGUI.get().getDescriptionColor().getRGB(),
                                ClickGUI.get().getDescriptionOutlineColor().getRGB());
                        renderer.drawString(
                                !ClickGUI.get().lowercaseDescriptions.getValue() ? item.getProperty().getDescription() : item.getProperty().getDescription().toLowerCase(),
                                2,
                                resolution.getScaledHeight() - 10,
                                ClickGUI.get().getDescriptionTextColor().getRGB());
                    }
                    GlStateManager.translate(0.0f, 0.0f, -1.0f);

                    item.drawScreen(mouseX, mouseY, partialTicks);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!items.isEmpty()) {
            if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
                subOpen = !subOpen;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            if (subOpen) {
                for (Item item : items) {
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        items.forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
        super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (!subOpen) {
            return;
        }
        items.forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    @Override
    public float getHeight() {
        if (subOpen) {
            float height = 14;
            for (Item item : items) {
                height += item.getHeight() + 1.5f;
            }
            return height + 2;
        }
        return 14;
    }

    @Override
    public void toggle() {
        module.toggle();
    }

    @Override
    public boolean getState() {
        return module.isEnabled();
    }
}
