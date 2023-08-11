package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.impl.event.events.movement.actions.JumpEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

public class ListenerJump extends ModuleListener<Announcer, JumpEvent> {
    public ListenerJump(Announcer module) {
        super(module, JumpEvent.class);
    }

    @Override
    public void call(JumpEvent event) {
        if (module.jump.getValue() && module.jumpTimer.passed(module.delay.getValue() * 2000)) {
            module.addEvent(Type.JUMP);
        }
    }
}
