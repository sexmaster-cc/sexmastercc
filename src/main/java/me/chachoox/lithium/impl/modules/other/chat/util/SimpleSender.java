package me.chachoox.lithium.impl.modules.other.chat.util;

import me.chachoox.lithium.api.util.thread.SafeRunnable;

import static me.chachoox.lithium.impl.modules.other.chat.ChatBridge.sendSimpleMessage;

public class SimpleSender implements SafeRunnable {
    private final String user;
    private final String message;

    public SimpleSender(String user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public void runSafely() {
        try {
            sendSimpleMessage(user, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}