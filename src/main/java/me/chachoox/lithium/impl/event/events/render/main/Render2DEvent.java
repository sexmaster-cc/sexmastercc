package me.chachoox.lithium.impl.event.events.render.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent {
    public ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }
}
