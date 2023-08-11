package me.chachoox.lithium.api.module;

import me.chachoox.lithium.api.util.logger.Logger;

public class PersistentModule extends Module {
    public PersistentModule(String label, String[] aliases, String description, Category category) {
        super(label, aliases, description, category);
        enable();
    }

    @Override
    public void disable() {
        Logger.getLogger().log("You cant disable this module");
    }
}
