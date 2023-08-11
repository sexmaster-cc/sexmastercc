package me.chachoox.lithium.asm.mixins.gui;

import me.chachoox.lithium.impl.gui.entity.DupeButton;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Comparator;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {

    private DupeButton salGuiDupeButton;

    @Shadow
    protected int guiLeft;

    @Shadow
    protected int guiTop;

    @Inject(method = "initGui", at = @At("HEAD"))
    public void initGui(CallbackInfo info) {
        buttonList.clear();
        salGuiDupeButton = new DupeButton(1338, this.width / 2 - 50, this.guiTop - 20, "Dupe");
        this.buttonList.add(salGuiDupeButton);
        salGuiDupeButton.setWidth(100);
        updateButton();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1338) {
            doDupe();
        } else {
            super.actionPerformed(button);
        }

    }

    @Inject(method = "updateScreen", at = @At("HEAD"))
    public void updateScreen(CallbackInfo ci) {
        updateButton();
    }

    private void updateButton() {
        if (Managers.DUMMY.isValid()) {
            salGuiDupeButton.visible = true;
            salGuiDupeButton.displayString = "Dupe";
        } else {
            salGuiDupeButton.visible = false;
        }
    }

    public void doDupe() {
        Entity daHorse = mc.world.loadedEntityList.stream()
                .filter(this::isValidEntity)
                .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                .orElse(null);

        if (mc.currentScreen instanceof GuiScreenHorseInventory && daHorse instanceof AbstractChestHorse && mc.player.getRidingEntity() != null) {
            AbstractChestHorse abstractChestHorse = (AbstractChestHorse) daHorse;

            if (abstractChestHorse.hasChest()) {
                mc.player.connection.sendPacket(new CPacketUseEntity(daHorse, EnumHand.MAIN_HAND, daHorse.getPositionVector()));
            }

        }
    }

    private boolean isValidEntity(Entity entity) {
        if (entity instanceof AbstractChestHorse) {
            AbstractChestHorse horse = (AbstractChestHorse) entity;
            return !horse.isChild() && horse.isTame();
        }

        return false;
    }

}
