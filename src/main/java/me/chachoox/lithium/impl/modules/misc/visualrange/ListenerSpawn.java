package me.chachoox.lithium.impl.modules.misc.visualrange;

import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.entity.EntityWorldEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;

public class ListenerSpawn extends ModuleListener<VisualRange, EntityWorldEvent.Add> {
    public ListenerSpawn(VisualRange module) {
        super(module, EntityWorldEvent.Add.class);
    }

     @Override
    public void call(EntityWorldEvent.Add event) {
         if (mc.player != null && event.getEntity() instanceof EntityGhast && module.ghasts.getValue()) {
             Entity ghast = event.getEntity();
             Logger.getLogger().log(TextColor.GOLD + "Ghast spotted at: "
                             + ((int) ghast.posX)
                             + ", "
                             + ((int) ghast.posY)
                             + ", "
                             + ((int) ghast.posZ)
                             + "."
                     , ghast.getEntityId()
             );
         }

         if (mc.player != null && event.getEntity() instanceof EntityPlayer) {
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
                                 module.message.getValue().getJoin()),
                         player.getName().hashCode());
             }

             if (module.sounds.getValue()) {
                 mc.player.playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
             }
         }
     }
}