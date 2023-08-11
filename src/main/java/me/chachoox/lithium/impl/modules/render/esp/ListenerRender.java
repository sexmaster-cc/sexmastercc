package me.chachoox.lithium.impl.modules.render.esp;

import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.esp.util.RenderMode;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Comparator;
import java.util.List;

public class ListenerRender extends ModuleListener<ESP, Render3DEvent> {
    public ListenerRender(ESP module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        final Entity renderEntity = RenderUtil.getEntity();
        final Frustum frustum = Interpolation.createFrustum(renderEntity);
        final List<Entity> entityList = mc.world.loadedEntityList;
        entityList.sort(Comparator.comparing(entity -> mc.player.getDistance((Entity) entity)).reversed());
        for (Entity entity : entityList) {
            AxisAlignedBB bb = entity.getEntityBoundingBox();
            Vec3d vec = Interpolation.interpolateEntity(entity);

            if (!frustum.isBoundingBoxInFrustum(bb)) {
                continue;
            }

            if (module.isValid(entity) && module.mode.getValue() != RenderMode.NONE) {
                module.doRender(new AxisAlignedBB(0.0, 0.0, 0.0, entity.width, entity.height, entity.width).offset(vec.x - (double) (entity.width / 2.0f), (vec.y + 0.05), vec.z - (double) (entity.width / 2.0f)).grow(0.05));
            }

            if (module.itemNametags.getValue() && entity instanceof EntityItem) {
                GL11.glPushMatrix();
                module.renderNameTag(((EntityItem) entity).getItem().getDisplayName() + " x" + ((EntityItem) entity).getItem().getCount(), vec.x, vec.y, vec.z);
                RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }

            if (module.chorus.getValue() && module.teleportPos != null) {
                double x = module.teleportPos.getX() - Interpolation.getRenderPosX();
                double y = module.teleportPos.getY() - Interpolation.getRenderPosY();
                double z = module.teleportPos.getZ() - Interpolation.getRenderPosZ();
                if (module.teleportTimer.passed(2500)) {
                    module.teleportPos = null;
                    return;
                }
                GL11.glPushMatrix();
                module.renderNameTag("Player Teleports", x, y, z);
                RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
        }
    }
}
