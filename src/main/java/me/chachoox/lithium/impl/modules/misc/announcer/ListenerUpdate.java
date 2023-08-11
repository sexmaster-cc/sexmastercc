package me.chachoox.lithium.impl.modules.misc.announcer;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.misc.announcer.util.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListenerUpdate extends ModuleListener<Announcer, UpdateEvent> {
    public ListenerUpdate(Announcer module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        module.setMessages();
        if (module.move.getValue() && module.moveTimer.passed(module.delay.getValue() * 1500)) {
            float dist = (float) module.speed;
            if (dist > 0.0) {
                module.events.put(Type.WALK, dist);
                module.speed = 0.0;
                module.moveTimer.reset();
            }
        }

        if (!module.events.isEmpty() && module.timer.passed(module.delay.getValue() * 1000)) {
            try { //i couldnt figure out what causes the crash soo
                int index = module.random.nextInt(module.events.entrySet().size());
                for (int i = 0; i < module.events.entrySet().size(); i++) {
                    if (i == index) {
                        List<Map.Entry<Type, Float>> list = new ArrayList<>(module.events.entrySet());
                        Map.Entry<Type, Float> entry = list.get(i);
                        if (entry.getValue() != null && entry.getKey() != null && !EntityUtil.isDead(mc.player)) {
                            mc.player.sendChatMessage((module.greenText.getValue() ? "> " : "") + module.getMessage(entry.getKey(), entry.getValue()));
                            if (module.cycle.getValue()) {
                                module.language.increment();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            module.timer.reset();
            module.events.clear();
        }
    }
}