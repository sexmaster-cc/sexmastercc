package me.chachoox.lithium.impl.modules.other.chat;

import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.chat.util.ComplexSender;

import java.awt.*;

public class ListenerWorldLoad extends ModuleListener<ChatBridge, WorldClientEvent.Load> {
    public ListenerWorldLoad(ChatBridge module) {
        super(module, WorldClientEvent.Load.class);
    }

    @Override
    public void call(WorldClientEvent.Load event) {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData() != module.data && !mc.isSingleplayer() && module.server.getValue()) {
            Managers.THREAD.submit(new ComplexSender(module.getUser(), "[Join]", mc.getCurrentServerData().serverIP, Color.GREEN));
            module.data = mc.getCurrentServerData();
        }
    }
}
