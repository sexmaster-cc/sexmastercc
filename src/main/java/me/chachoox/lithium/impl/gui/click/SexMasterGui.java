package me.chachoox.lithium.impl.gui.click;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.gui.click.item.Item;
import me.chachoox.lithium.impl.gui.click.item.ModuleButton;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.clickgui.ClickGUI;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class SexMasterGui extends GuiScreen {
    private static SexMasterGui clickGui;
    private final ArrayList<Panel> panels;

    public SexMasterGui() {
        panels = new ArrayList<>();
        load();
    }

    public static SexMasterGui getClickGui() {
        return (SexMasterGui.clickGui == null) ? (SexMasterGui.clickGui = new SexMasterGui()) : SexMasterGui.clickGui;
    }

    private void load() {
        int x = -100;
        for (Category category : Category.values()) {
            panels.add(new Panel(category.getLabel(), x += 102, 4, true) {
                @Override
                public void setupItems() {
                    for (Module modules : Managers.MODULE.getModules()) {
                        if (!modules.getCategory().equals(category)) {
                            continue;
                        }
                        addButton(new ModuleButton(modules));
                    }
                }
            });
        }
        panels.forEach(panel -> panel.getItems().sort(Comparator.comparing(Item::getLabel)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGUI clickGUI = ClickGUI.get();
        if (clickGUI.gradient.getValue()) {
            Render2DUtil.drawGradientRect(0, 0, mc.displayWidth, mc.displayHeight, clickGUI.sideWays.getValue(), clickGUI.gradientFirstColor.getColor().getRGB(), clickGUI.gradientSecondColor.getColor().getRGB());
        } else {
            Render2DUtil.drawRect(0, 0, mc.displayWidth, mc.displayHeight, clickGUI.gradientFirstColor.getColor().getRGB());
        }
        panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    public void initGui() {
        if (ClickGUI.get().blur.getValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            ClickGUI.get().loaded = true;
        }
    }

    @Override
    public void onGuiClosed() {
        if (ClickGUI.get().loaded) {
            mc.entityRenderer.stopUseShader();
            ClickGUI.get().loaded = false;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        panels.forEach(panel -> panel.onKeyTyped(typedChar, keyCode));
    }

    public void handleMouseInput() throws IOException {
        int scrollAmount = 5;
        if (Mouse.getEventDWheel() > 0) {
            for (Panel panel : panels) {
                panel.setY(panel.getY() + scrollAmount);
            }
        }
        if (Mouse.getEventDWheel() < 0) {
            for (Panel panel : panels) {
                panel.setY(panel.getY() - scrollAmount);
            }
        }

        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<Panel> getPanels() {
        return panels;
    }
}
