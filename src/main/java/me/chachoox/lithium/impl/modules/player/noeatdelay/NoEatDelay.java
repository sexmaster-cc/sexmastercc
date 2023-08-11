package me.chachoox.lithium.impl.modules.player.noeatdelay;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

public class NoEatDelay extends Module {

    public NoEatDelay() {
        super("NoEatDelay", new String[]{"NoEatDelay", "AntiEatDelay", "AutoFat", "PollosFavoriteModule"}, "Removes eating delay interval.", Category.PLAYER);
    }
}
