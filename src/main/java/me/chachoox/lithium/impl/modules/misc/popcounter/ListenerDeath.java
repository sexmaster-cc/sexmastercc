package me.chachoox.lithium.impl.modules.misc.popcounter;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDeath extends ModuleListener<PopCounter, DeathEvent> {
    public ListenerDeath(PopCounter module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        EntityLivingBase player = event.getEntity();
        if (player instanceof EntityPlayer) {
            String name = player.getName();
            if (Managers.TOTEM.getPopMap().containsKey(name)) {
                boolean isSelf = player == mc.player;
                boolean friend = isSelf || Managers.FRIEND.isFriend(name);
                int pops = Managers.TOTEM.getPopMap().get(name);

                if (module.ordinalNumbers.getValue()) {
                    module.sendMessage((friend ? module.getFriendColor() : module.getPlayerColor())
                            + ((isSelf ? "You" : name)
                            + module.getTotemColor()
                            + " died after" + (isSelf ? " popping your " : " popping their ")
                            + module.getFinalColor() + pops
                            + module.getNumberStringThing(pops) + module.getTotemColor()
                            + " totem!"), -player.getEntityId());
                } else {
                    module.sendMessage((friend ? module.getFriendColor() : module.getPlayerColor())
                            + ((isSelf ? "You" : name)
                            + module.getTotemColor()
                            + " died after popping "
                            + module.getFinalColor()
                            + pops
                            + module.getTotemColor()
                            + (pops == 1 ? " totem!" : " totems!")), -player.getEntityId());
                }
            }
        }
    }
}
