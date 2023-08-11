package me.chachoox.lithium.impl.modules.combat.selffill;

import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.AttackUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ListenerMotion extends ModuleListener<SelfFill, MotionUpdateEvent> {
    public ListenerMotion(SelfFill module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (module.wait.getValue()) {
            BlockPos currentPos = PositionUtil.getPosition();
            if (!currentPos.equals(module.startPos)) {
                module.disable();
                return;
            }
        }

        if (!module.timer.passed(module.delay.getValue())) {
            return;
        }

        if (module.isInsideBlock()) {
            return;
        }

        BlockPos pos = PositionUtil.getPosition();
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable() || mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            if (!module.wait.getValue()) {
                module.disable();
            }
            return;
        }

        BlockPos posDown = pos.down();
        if (mc.world.getBlockState(posDown).getBlock() instanceof BlockLiquid || mc.world.getBlockState(posDown).getBlock() instanceof BlockAir) {
            if (!module.wait.getValue()) {
                module.disable();
            }
            return;
        }

        BlockPos posHead = pos.up(2);
        if (!mc.world.getBlockState(posHead).getMaterial().isReplaceable() && module.wait.getValue()) {
            return;
        }

        BlockPos upUp = pos.up(2);
        IBlockState upState = mc.world.getBlockState(upUp);
        if (upState.getMaterial().blocksMovement() || mc.world.getBlockState(upUp).getBlock() instanceof BlockLiquid) {
            if (!module.wait.getValue()) {
                module.disable();
            }
            return;
        }

        int startSlot = mc.player.inventory.currentItem;

        int slot = -1;

        for (int i = 9; i >= 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                if (module.isValid(((ItemBlock) mc.player.inventory.getStackInSlot(i).getItem()).getBlock())) {
                    slot = i;
                }
            }
        }

        if (slot == -1) {
            Logger.getLogger().log(TextColor.RED + "<SelfFill>" + " No valid blocks.");
            module.disable();
            return;
        }

        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity != null && !mc.player.equals(entity) && !EntityUtil.isDead(entity) && entity.preventEntitySpawning) {
                if (!module.wait.getValue()) {
                    module.disable();
                }
                return;
            }
        }

        if (module.attack.getValue() && module.breakTimer.passed(module.attackDelay.getValue())) {
            AttackUtil.attackInPos(pos, module.rotate.getValue() ? RotationsEnum.PACKET : RotationsEnum.NONE, module.swing.getValue(), false);
            module.breakTimer.reset();
        }

        if (AttackUtil.isInterceptedByCrystal(pos) && module.strict.getValue()) {
            module.confirmTimer.reset();
            if (!module.wait.getValue()) {
                module.disable();
            }
            return;
        }

        if (!module.confirmTimer.passed(250) && module.strict.getValue()) {
            return;
        }

        EnumFacing facing = BlockUtil.getFacing(pos);
        if (facing == null) {
            if (!module.wait.getValue()) {
                module.disable();
            }
            return;
        }

        ItemStack oldItem = mc.player.getHeldItemMainhand();

        if (module.altSwap.getValue()) {
            ItemUtil.switchToAlt(slot);
        } else {
            ItemUtil.switchTo(slot);
        }

        ItemStack newItem = mc.player.getHeldItemMainhand();

        if (module.rotate.getValue()) {
            BlockPos newPos = module.startPos.offset(facing);
            float[] angles = RotationUtil.getRotations(newPos, facing.getOpposite(), mc.player);
            Managers.ROTATION.setRotations(angles[0], angles[1]);
            PacketUtil.send(new CPacketPlayer.Rotation(angles[0], angles[1], mc.player.onGround));
        }

        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999848688698, mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7500015, mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.999997, mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.17000300178814, mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.170010501788138, mc.player.posZ, false));

        BlockPos finalPos = pos.offset(facing);
        float[] rotations = RotationUtil.getRotations(finalPos, facing.getOpposite(), mc.player);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1]);
        float[] vec = RaytraceUtil.hitVecToPlaceVec(finalPos, result.hitVec);

        PacketUtil.sneak(true);

        PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(finalPos, facing.getOpposite(), EnumHand.MAIN_HAND, vec[0], vec[1], vec[2]));

        switch (module.swing.getValue()) {
            case NORMAL: {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            }
            case PACKET: {
                PacketUtil.swing();
                break;
            }
        }

        PacketUtil.sneak(false);

        //PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.242610501394747, mc.player.posZ, false));
        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, module.getY(mc.player), mc.player.posZ, false));

        if (startSlot != -1) {
            if (module.altSwap.getValue()) {
                short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                int newSlot = ItemUtil.hotbarToInventory(slot);
                int altSlot = ItemUtil.hotbarToInventory(startSlot);
                Slot currentSlot = mc.player.inventoryContainer.inventorySlots.get(altSlot);
                Slot swapSlot = mc.player.inventoryContainer.inventorySlots.get(newSlot);
                PacketUtil.send(new CPacketClickWindow(0, newSlot, mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                currentSlot.putStack(oldItem);
                swapSlot.putStack(newItem);
            } else {
                ItemUtil.switchTo(startSlot);
            }
        }

        module.timer.reset();
        if (!module.wait.getValue()) {
            module.disable();
        }
    }

}
