package me.chachoox.lithium.impl.modules.movement.fly;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;

public class ListenerMove extends ModuleListener<Fly, MoveEvent> {
    public ListenerMove(Fly module) {
        super(module, MoveEvent.class, 1500);
    }

    @Override
    public void call(MoveEvent event) {
        double speed = module.speed.getValue();
        event.setX(event.getX() * speed);
        event.setZ(event.getZ() * speed);
    }
}
