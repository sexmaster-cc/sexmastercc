package me.chachoox.lithium.impl.modules.other.chat;

import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.chat.util.ComplexSender;

import java.awt.*;

public class ListenerDisconnect extends ModuleListener<ChatBridge, DisconnectEvent> {
    public ListenerDisconnect(ChatBridge module) {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void call(DisconnectEvent event) {
        if (module.data != null && !mc.isSingleplayer() && module.server.getValue()) {
            Managers.THREAD.submit(new ComplexSender(module.getUser(), "[Leave]", module.data.serverIP, Color.RED));
            module.data = null;
        }
    }
}
