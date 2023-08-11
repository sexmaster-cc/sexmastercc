package me.chachoox.lithium.impl.modules.misc.spammer;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextUtil;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import org.apache.logging.log4j.Level;

import java.util.concurrent.ThreadLocalRandom;

public class ListenerTick extends ModuleListener<Spammer,TickEvent> {
    public ListenerTick(Spammer module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (module.isNull()) {
            module.disable();
            return;
        }

        if (module.currentFile == null) {
            Logger.getLogger().log("You have to load a file to use this module");
            module.disable();
            return;
        }

        try {
            if (module.timer.passed(module.delay.getValue() * 1000)) {
                int line = module.randomize.getValue() ? ThreadLocalRandom.current().nextInt(module.strings.size()) : 0;
                String greenText = module.greenText.getValue() ? "> " : "";
                String text = module.strings.get(line);
                String antiKick = (module.antiKick.getValue() ? TextUtil.generateRandomString(5) : "");
                mc.player.sendChatMessage(greenText + text + antiKick);

                if (!module.randomize.getValue()) {
                    module.strings.remove(text);
                    module.strings.add(text);
                }

                module.timer.reset();
            }
        } catch (Exception e) {
            Logger.getLogger().log(Level.ERROR, "Couldn't send spammer message");
        }
    }
}
