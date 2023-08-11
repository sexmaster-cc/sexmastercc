package me.chachoox.lithium.impl.modules.misc.visualrange;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDespawn extends ModuleListener<VisualRange, EntityWorldEvent.Remove> {
    public ListenerDespawn(VisualRange module) {
        super(module, EntityWorldEvent.Remove.class);
    }

    @Override
    public void call(EntityWorldEvent.Remove event) {
        if (mc.player != null && event.getEntity() instanceof EntityPlayer && module.left.getValue()) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (player != null && !player.getName().equalsIgnoreCase(mc.player.getName())) {
                if (module.onlyGeared.getValue() && EntityUtil.isNaked(player)) {
                    return;
                }

                boolean isFriend = Managers.FRIEND.isFriend(player.getName());
                module.sendMessage(String.format("%s%s%s%s",
                                isFriend ? module.getFriendColor() : module.getNameColor(),
                                player.getName(),
                                module.getBridgeColor(),
                                module.message.getValue().getLeave()),
                        player.getName().hashCode());
            }
        }
    }
}