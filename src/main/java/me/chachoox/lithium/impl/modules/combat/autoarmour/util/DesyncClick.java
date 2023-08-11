package me.chachoox.lithium.impl.modules.combat.autoarmour.util;

import me.chachoox.lithium.api.util.math.TimeStamp;

public class DesyncClick extends TimeStamp {
    private final WindowClick click;

    public DesyncClick(WindowClick click) {
        this.click = click;
    }

    public WindowClick getClick() {
        return click;
    }

}
