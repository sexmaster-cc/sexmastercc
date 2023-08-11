package me.chachoox.lithium.impl.event.events.movement;

import me.chachoox.lithium.api.event.events.Event;
import net.minecraft.util.MovementInput;

public class InputUpdateEvent extends Event {
    private final MovementInput input;

    public InputUpdateEvent(MovementInput input) {
        this.input = input;
    }

    public MovementInput getMovementInput() {
        return input;
    }
}
