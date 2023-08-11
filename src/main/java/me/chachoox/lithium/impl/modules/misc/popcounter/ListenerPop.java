package me.chachoox.lithium.impl.modules.misc.popcounter;

import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerPop extends ModuleListener<PopCounter, TotemPopEvent> {
    public ListenerPop(PopCounter module) {
        super(module, TotemPopEvent.class);
    }

    @Override
    public void call(TotemPopEvent event) {
        EntityPlayer entity = event.getEntity();
        String name = entity.getName();
        boolean isSelf = entity == mc.player;
        boolean friend = isSelf || Managers.FRIEND.isFriend(name);
        int pops = Managers.TOTEM.getPopMap().get(name);

        if (module.ordinalNumbers.getValue()) {
            module.sendMessage((friend ? module.getFriendColor() : module.getPlayerColor())
                    + ((isSelf ? "You" : name)
                    + module.getTotemColor()
                    + (isSelf ? " popped your " : " popped their ")
                    + module.getNumberColor()
                    + pops + module.getNumberStringThing(pops)
                    + module.getTotemColor()
                    + " totem!"), -entity.getEntityId());
        } else {
            module.sendMessage((friend ? module.getFriendColor() : module.getPlayerColor())
                    + ((isSelf ? "You" : name)
                    + module.getTotemColor()
                    + (isSelf ? " popped " : " has popped ")
                    + module.getNumberColor()
                    + pops
                    + module.getTotemColor()
                    + (pops == 1 ? " time in total!" : " times in total!")), -entity.getEntityId());
        }
    }
}
