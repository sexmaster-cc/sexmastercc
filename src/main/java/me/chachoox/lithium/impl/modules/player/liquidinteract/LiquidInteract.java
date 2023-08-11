package me.chachoox.lithium.impl.modules.player.liquidinteract;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;

public class LiquidInteract extends Module {

    public LiquidInteract() {
        super("LiquidInteract", new String[]{"LiquidInteract", "LiquidPlace", "LiquidBlock"}, "Allows you to place blocks on the side of liquids.", Category.PLAYER);
    }
}