package me.chachoox.lithium.impl.modules.combat.aura;

import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.render.Interpolation;
import me.chachoox.lithium.api.util.render.RenderUtil;
import me.chachoox.lithium.impl.event.events.render.main.Render3DEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.aura.modes.SwordMode;
import me.chachoox.lithium.impl.modules.other.colours.Colours;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class ListenerRender extends ModuleListener<Aura, Render3DEvent> {
    public ListenerRender(Aura module) {
        super(module, Render3DEvent.class);
    }

    @Override
    public void call(Render3DEvent event) {
        if (module.render.getValue() && module.target != null) {
            if (module.sword.getValue() != SwordMode.NONE) {
                if (ItemUtil.isHolding(ItemSword.class)) {
                    Vec3d vec = Interpolation.interpolateEntity(module.target);
                    RenderUtil.startRender();
                    renderBox(module.target, vec);
                    RenderUtil.endRender();
                }
            } else {
                RenderUtil.startRender();
                Vec3d vec = Interpolation.interpolateEntity(module.target);
                renderBox(module.target, vec);
                RenderUtil.endRender();
            }
        }
    }
    private void renderBox(Entity entity, Vec3d vec) {
        RenderUtil.drawBox(new AxisAlignedBB(0.0,
                0.0,
                0.0,
                entity.width,
                entity.height,
                entity.width).offset(vec.x - (double)(entity.width / 2.0F), vec.y, vec.z - (double)(entity.width / 2.0F)).grow(0.05), Colours.get().getColourCustomAlpha(60));
    }
}
