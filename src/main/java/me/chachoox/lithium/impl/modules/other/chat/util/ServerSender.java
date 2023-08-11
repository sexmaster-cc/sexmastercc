package me.chachoox.lithium.impl.modules.other.chat.util;

import me.chachoox.lithium.api.util.thread.SafeRunnable;

import java.awt.*;

import static me.chachoox.lithium.impl.modules.other.chat.ChatBridge.sendServerMessage;

public class ServerSender implements SafeRunnable {
    private final String user;
    private final String message;
    private final String server;
    private final Color color;

    public ServerSender(String user, String message, String server, Color color) {
        this.user = user;
        this.message = message;
        this.server = server;
        this.color = color;
    }

    @Override
    public void runSafely() {
        try {
            sendServerMessage(user, message, server, color);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}