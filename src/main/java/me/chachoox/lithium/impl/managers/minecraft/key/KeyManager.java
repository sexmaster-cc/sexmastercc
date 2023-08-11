package me.chachoox.lithium.impl.managers.minecraft.key;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;

public class KeyManager extends SubscriberImpl implements Minecraftable {

    private boolean keyDown;
    public static KeyBinding KIT_DELETE_BIND;

    public KeyManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class) {
            @Override
            public void call(TickEvent event) {
                if (mc.currentScreen instanceof GuiContainer && Keyboard.isKeyDown(KIT_DELETE_BIND.getKeyCode())) {
                    Slot slot = ((GuiContainer) mc.currentScreen).getSlotUnderMouse();
                    if (slot == null || keyDown) {
                        return;
                    }
                    mc.player.sendChatMessage("/deleteukit " + TextUtil.removeColor(slot.getStack().getDisplayName()));
                    keyDown = true;
                } else if (keyDown) {
                    keyDown = false;
                }
            }
        });
    }
}
