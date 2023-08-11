package me.chachoox.lithium.impl.modules.misc.autoreply;

public enum ReplyMessages {
    LITHIUM("[Lithium] I'm afk."),
    SEXMASTER("[SexMaster.CC] I'm afk."),
    POLLOS("im gay."),// wtff
    MONEY("hello money gang"),
    COORDS("<Coords>");

    private final String reply;

    ReplyMessages(String message) {
        this.reply = message;
    }

    public String getReply() {
        return reply;
    }
}
