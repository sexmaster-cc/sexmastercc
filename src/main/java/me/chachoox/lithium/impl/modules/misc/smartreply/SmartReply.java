package me.chachoox.lithium.impl.modules.misc.smartreply;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
// TODO: make a target for this like ---> (,smartreply target set <name>) and it forces the reply to that and nobody else until u reset it
public class SmartReply extends Module {

    protected String whisperSender;
    protected boolean whispered;

    public SmartReply() {
        super("SmartReply", new String[]{"SmartReply", "betterreply", "ignoreunicode"}, "Replies but smarter.", Category.MISC);
        this.offerListeners(new ListenerSChat(this), new ListenerCChat(this));
    }

    @Override
    public String getSuffix() {
        return whisperSender;
    }
}
