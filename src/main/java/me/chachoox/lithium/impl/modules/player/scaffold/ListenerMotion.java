package me.chachoox.lithium.impl.modules.player.scaffold;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.asm.ducks.IMinecraft;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.apache.logging.log4j.Level;

public class ListenerMotion extends ModuleListener<Scaffold, MotionUpdateEvent> {

    public ListenerMotion(Scaffold module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            module.facing = null;
            BlockPos prev = module.pos;
            module.pos = null;
            module.pos = module.findNextPos();
            if (module.pos != null) {
                module.rot = module.pos;
                if (!module.pos.equals(prev)) {
                    module.rotationTimer.reset();
                }
                setRotations(module.pos);
            } else if (module.rot != null && module.rotation.getValue() != RotationsEnum.NONE && !module.rotationTimer.passed(500)) {
                setRotations(module.rot);
            } else {
                module.rot = null;
            }
        } else {
            if (module.pos == null || module.facing == null && module.rotation.getValue() != RotationsEnum.NONE) {
                return;
            }

            int slot = -1;

            for (int i = 9; i >= 0; --i) {
                if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                    if (module.isValid(((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) {
                        slot = i;
                        if (i == mc.player.inventory.currentItem) {
                            break;
                        }
                    }
                }
            }

            if (slot != -1) {
                boolean jump = mc.player.movementInput.jump && module.tower.getValue();
                boolean sneak = mc.player.movementInput.sneak && module.down.getValue();
                if (jump && !sneak && !MovementUtil.isMoving()) {
                    ((IMinecraft) mc).setRightClickDelay(3);
                    mc.player.jump();
                    if (module.towerTimer.passed(1500)) {
                        mc.player.motionY = -0.28;
                        module.towerTimer.reset();
                    }
                    placeBlocks(slot, true);
                } else {
                    module.towerTimer.reset();
                }
                try {
                    if (module.placeTimer.passed(module.delay.getValue())) {
                        placeBlocks(slot, false);
                    }
                } catch (Exception e) {
                    Logger.getLogger().log(Level.ERROR, e.getMessage());
                    Logger.getLogger().log(TextColor.RED + "Error while placing block at X: " + module.pos.getX() + " Y: " + module.pos.getY() + " Z: " + module.pos.getZ() + ".");
                }
            }
        }
    }

    private void setRotations(BlockPos pos) {
        module.facing = BlockUtil.getFacing(pos);
        if (module.facing != null) {
            setRotations(pos, module.facing);
            return;
        }

        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos p = pos.offset(facing);
            EnumFacing f = BlockUtil.getFacing(p);
            if (f != null) {
                module.facing = f;
                module.pos = p;
                setRotations(p, f);
            }
        }
    }

    private void setRotations(BlockPos pos, EnumFacing facing) {
        module.rotations = RotationUtil.getRotations(pos.offset(facing), facing.getOpposite());
        if (module.rotation.getValue() != RotationsEnum.NONE && module.rotations != null) {
            RotationUtil.doRotation(module.rotation.getValue(), module.rotations);
        }
    }

    private void placeBlocks(int slot, boolean tower) {
        int lastSlot = mc.player.inventory.currentItem;
        ItemUtil.switchTo(slot);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(module.rotations[0], module.rotations[1]);
        if (module.clickTimer.passed(!tower ? 25 : 0)) {
            try {
                mc.playerController.processRightClickBlock(mc.player, mc.world, module.pos.offset(module.facing), module.facing.getOpposite(), result.hitVec, ItemUtil.getHand(slot));
                module.placeTimer.reset();
            } catch (Exception e) {
                Logger.getLogger().log(Level.ERROR, e.getMessage());
                Logger.getLogger().log(TextColor.RED + "Error while placing block at X: " + module.pos.getX() + " Y: " + module.pos.getY() + " Z: " + module.pos.getZ() + ".");
            }
            module.clickTimer.reset();
        }
        ItemUtil.switchTo(lastSlot);
        switch (module.swing.getValue()) {
            case NONE: {
                break;
            }
            case NORMAL: {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case PACKET: {
                PacketUtil.swing();
                break;
            }
        }
    }
}