package me.chachoox.lithium.impl.modules.other.chat;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.chat.util.ServerSender;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class ListenerUpdate extends ModuleListener<ChatBridge, UpdateEvent> {
    public ListenerUpdate(ChatBridge module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (module.timer.passed(60000L * module.tabDelay.getValue()) && module.tab.getValue() && module.data != null) {

            List<String> players = new ArrayList<>();
            for (NetworkPlayerInfo networkPlayerInfo : mc.player.connection.getPlayerInfoMap()) {
                if (networkPlayerInfo == null) continue;
                String name = networkPlayerInfo.getGameProfile().getName();
                players.add(name);
            }

            if (players.isEmpty()) {
                module.timer.reset();
                return;
            }

            int count = players.size();
            players.sort(Comparator.comparing(player -> player));
            StringJoiner stringJoiner = new StringJoiner(", ");
            players.forEach(stringJoiner::add);

            Managers.THREAD.submit(new ServerSender(module.getUser(), String.format("[Players] (%s) %s", count, stringJoiner), module.data.serverIP, Color.PINK));
            module.timer.reset();
        }
    }
}
