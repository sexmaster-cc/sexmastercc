package me.chachoox.lithium.api.util.inventory;

import me.chachoox.lithium.api.interfaces.Minecraftable;

//yes this is very stupid ik
public class SwitchUtil implements Minecraftable {

    public static void doSwitch(Swap swap, int slot) {
        switch (swap) {
            case SILENT: {
                ItemUtil.switchTo(slot);
                break;
            }
            case ALTERNATIVE: {
                ItemUtil.switchToAlt(slot);
                break;
            }
        }
    }
}
