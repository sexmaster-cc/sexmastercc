package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.screen.GuiScreenEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.client.gui.GuiGameOver;

public class ListenerGui extends ModuleListener<AutoLog, GuiScreenEvent<GuiGameOver>> {
    public ListenerGui(AutoLog module) {
        super(module, GuiScreenEvent.class, GuiGameOver.class);
    }

    @Override
    public void call(GuiScreenEvent<GuiGameOver> event) {
        Logger.getLogger().log(TextColor.RED + "<AutoLog> Disabling," + " try setting your health higher!");
        module.disable();
    }
}

