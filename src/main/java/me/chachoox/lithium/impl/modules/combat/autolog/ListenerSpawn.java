package me.chachoox.lithium.impl.modules.combat.autolog;

import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerSpawn extends ModuleListener<AutoLog, EntityWorldEvent.Add> {
    public ListenerSpawn(AutoLog module) {
        super(module, EntityWorldEvent.Add.class);
    }

    @Override
    public void call(EntityWorldEvent.Add event) {
        if (event.getEntity() instanceof EntityPlayer
                && !Managers.FRIEND.isFriend((EntityPlayer) event.getEntity())
                && module.onRender.getValue()
                && event.getEntity() != mc.player) {

            String disconnectMessage =
                    "Logged because -> ("
                    + event.getEntity().getName()
                    + ") came into render distance";

            NetworkUtil.disconnect(disconnectMessage);
            module.disable();
        }
    }
}
