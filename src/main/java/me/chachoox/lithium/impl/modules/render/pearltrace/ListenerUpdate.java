package me.chachoox.lithium.impl.modules.render.pearltrace;

import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.pearltrace.util.ThrownEntity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class ListenerUpdate extends ModuleListener<PearlTrace, UpdateEvent> {
    public ListenerUpdate(PearlTrace module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {

        mc.world.loadedEntityList.stream()
                .filter(entity -> mc.player != entity)
                .forEach(entity -> {
                    if (entity.ticksExisted > 1 && entity instanceof EntityEnderPearl) {
                        if (!module.thrownEntities.containsKey(entity.getEntityId())) {
                            final ArrayList<Vec3d> list = new ArrayList<>();
                            list.add(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ));
                            module.thrownEntities.put(entity.getEntityId(), new ThrownEntity(System.currentTimeMillis(), list));
                        } else {
                            module.thrownEntities.get(entity.getEntityId()).getVertices().add(new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ));
                            module.thrownEntities.get(entity.getEntityId()).setTime(System.currentTimeMillis());
                        }
                    }

                });
        module.thrownEntities.forEach((id, thrownEntity) -> {
            if (System.currentTimeMillis() - thrownEntity.getTime() > module.timeout.getValue()) {
                module.thrownEntities.remove(id);
            }
        });

    }
}