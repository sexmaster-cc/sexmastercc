package me.chachoox.lithium.impl.modules.misc.stresser;

import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerPop extends ModuleListener<Stresser, TotemPopEvent> {
    public ListenerPop(Stresser module) {
        super(module, TotemPopEvent.class);
    }

    @Override
    public void call(TotemPopEvent event) {
        final EntityPlayer player = event.getEntity();
        if (module.unicode.getValue()
                && module.timer.passed(module.delay.getValue() * 1000)
                && mc.player != null && !mc.player.equals(player)
                && !Managers.FRIEND.isFriend(player.getName())
                && module.sent.add(player.getName())) {
            mc.player.sendChatMessage("/msg " + player.getName() + " " + Stresser.LAG_MESSAGE);
            module.timer.reset();
        }
    }
}
