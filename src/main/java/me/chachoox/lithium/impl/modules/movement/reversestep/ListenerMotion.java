package me.chachoox.lithium.impl.modules.movement.reversestep;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.selffill.SelfFill;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityEnderPearl;

import java.util.List;
import java.util.stream.Collectors;

public class ListenerMotion extends ModuleListener<ReverseStep, MotionUpdateEvent> {
    public ListenerMotion(ReverseStep module) {
        super(module, MotionUpdateEvent.class);
    }

    private boolean reset = false;

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.POST) {
            if (EntityUtil.inLiquid(true)
                    || EntityUtil.inLiquid(false)
                    || Managers.MODULE.get(NoClip.class).isEnabled()
                    || Managers.MODULE.get(SelfFill.class).isEnabled()
                    || Managers.MODULE.get(Speed.class).isEnabled()
                    || mc.player.noClip) { //this is to make it work with pfly
                reset = true;
                return;
            }

            List<EntityEnderPearl> pearls = mc.world.loadedEntityList.stream()
                    .filter(EntityEnderPearl.class::isInstance)
                    .map(EntityEnderPearl.class::cast)
                    .collect(Collectors.toList());
            if (!pearls.isEmpty()) {
                module.waitForOnGround = true;
            }

            if (!mc.player.onGround) {
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    module.jumped = true;
                }
            } else {
                module.jumped = false;
                reset = false;
                module.waitForOnGround = false;
            }

            if (!module.jumped && mc.player.fallDistance < 0.5 && mc.player.posY - BlockUtil.getNearestBlockBelow() > 0.625 && mc.player.posY - BlockUtil.getNearestBlockBelow() <= module.distance.getValue() && !reset && !module.waitForOnGround) {
                if (!mc.player.onGround) {
                    module.packets++;
                }
                if (!mc.player.onGround && mc.player.motionY < 0 && !(mc.player.isOnLadder()
                        || mc.player.isEntityInsideOpaqueBlock())
                        && (!mc.player.isInsideOfMaterial(Material.LAVA)
                        && !mc.player.isInsideOfMaterial(Material.WATER))
                        && !mc.gameSettings.keyBindJump.isKeyDown() && module.packets > 0) {
                    mc.player.motionY = -module.speed.getValue();
                    module.packets = 0;
                }
            }
        }
    }
}
