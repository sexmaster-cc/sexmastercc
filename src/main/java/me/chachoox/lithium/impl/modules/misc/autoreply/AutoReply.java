package me.chachoox.lithium.impl.modules.misc.autoreply;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.util.math.StopWatch;

/**
 * @author moneymaker552
 */
public class AutoReply extends Module {

    protected final EnumProperty<ReplyMessages> message =
            new EnumProperty<>(
                    ReplyMessages.LITHIUM,
                    new String[]{"Message", "mode", "type", "response", "m"},
                    "How we want to reply to the message."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    5, 1, 15,
                    new String[]{"Delay", "del", "d"},
                    "How long we want to wait until responding to another message."
            );

    protected final StopWatch timer = new StopWatch();

    public AutoReply() {
        super("AutoReply", new String[]{"AutoReply", "reply", "replyback"}, "Automatically replies to whispers", Category.MISC);
        this.offerProperties(message, delay);
        this.offerListeners(new ListenerMessage(this));
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public String getSuffix() {
        return message.getFixedValue();
    }
}
