package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerUpdate extends ModuleListener<PvPInfo, UpdateEvent> {
    public ListenerUpdate(PvPInfo module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        if (!module.pearls()) {
            return;
        }

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderPearl) {
                EntityPlayer closest = null;
                for (EntityPlayer player : mc.world.playerEntities) {
                    if (closest == null || entity.getDistance(player) < entity.getDistance(closest)) {
                        closest = player;
                    }
                }

                if (closest != null && closest == mc.player) {
                    if (!module.pearlThrown) {
                        module.pearlTimer.reset();
                    }

                    module.pearlThrown = true;
                    return;
                }

                if (closest != null && closest.getDistance(entity) < 2 && !module.pearlsUUIDs.containsKey(entity.getUniqueID())) {
                    module.pearlsUUIDs.put(entity.getUniqueID(), 200);
                    Logger.getLogger().logNoMark(closest.getName() + " has just thrown a pearl!", false);
                }
            }
        }

        module.pearlsUUIDs.forEach((name, timeout) -> {
            if (timeout <= 0) {
                module.pearlsUUIDs.remove(name);
            } else {
                module.pearlsUUIDs.put(name, timeout - 1);
            }
        });

        if (module.pearlTimer.passed(15000)) {
            module.pearlThrown = false;
        }
    }
}
