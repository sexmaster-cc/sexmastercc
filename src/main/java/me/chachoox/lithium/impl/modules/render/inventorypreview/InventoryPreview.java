package me.chachoox.lithium.impl.modules.render.inventorypreview;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.render.Render2DUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render2DEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

// why are we back to renaming this again and again and again
public class InventoryPreview extends Module {

    private final Property<Boolean> shulkers =
            new Property<>(
                    false,
                    new String[]{"Shulkers", "shulkerboxes"},
                    "Shows you whats inside shulker boxes."
            );

    private final Property<Boolean> inv =
            new Property<>(
                    true,
                    new String[]{"Inv", "inventory"},
                    "Draws your inventory on the screen."
            );

    private final Property<Boolean> rect =
            new Property<>(
                    true,
                    new String[]{"Rect", "rectangle"},
                    "Draws a rectangle behind your inventory."
            );

    private final Property<Boolean> outlined =
            new Property<>(
                    true,
                    new String[]{"Outlined", "Outline"},
                    "Draws a outline around the rectangle."
            );

    private final NumberProperty<Integer> posX =
            new NumberProperty<>(
                    0,
                    -400, 400,
                    new String[]{"PosX", "x"},
                    "The x position of the inventory."
            );

    private final NumberProperty<Integer> posY =
            new NumberProperty<>(
                    0,
                    -400, 400,
                    new String[]{"PosY", "y"},
                    "The y position of the inventory."
            );

    public InventoryPreview() {
        super("InventoryPreview", new String[]{"InventoryPreview", "InvPreview", "ShulkerPreview", "ToolTips"}, "Draws better tooltips for inventories", Category.RENDER);
        this.offerProperties(shulkers, inv, rect, outlined, posX, posY);
        this.offerListeners(new LambdaListener<>(Render2DEvent.class, event -> {
            if (inv.getValue()) {
                int width = event.getResolution().getScaledWidth() / 2;
                int height = event.getResolution().getScaledHeight() / 2;
                int x = this.posX.getValue() + width;
                int y = this.posY.getValue() + height;

                if (this.rect.getValue()) {
                    if (!this.outlined.getValue()) {
                        Render2DUtil.drawRect(x, y, x + 148, y + 52, 0x3D000000);
                    } else {
                        Render2DUtil.drawBorderedRect(x, y, x + 148, y + 52, 0x3D000000, Colours.get().getColour().getRGB());
                    }
                }

                GlStateManager.pushMatrix();
                RenderHelper.enableGUIStandardItemLighting();

                for (int i = 0; i < 27; i++) {
                    ItemStack stack = mc.player.inventory.mainInventory.get(i + 9);

                    int itemPosX = x + (i % 9) * 16;
                    int itemPosY = y + (i / 9) * 16;

                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, itemPosX, itemPosY);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, itemPosX, itemPosY, null);
                }

                mc.getRenderItem().zLevel = -5.0f;
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popMatrix();
            }
        }));
    }

    public boolean isShulker() {
        return isEnabled() && shulkers.getValue();
    }
}
