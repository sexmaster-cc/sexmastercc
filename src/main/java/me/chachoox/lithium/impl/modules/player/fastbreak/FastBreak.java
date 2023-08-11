package me.chachoox.lithium.impl.modules.player.fastbreak;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.list.BlockList;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.inventory.Swap;
import me.chachoox.lithium.api.util.inventory.SwitchUtil;
import me.chachoox.lithium.api.util.list.ListEnum;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.RotationsEnum;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.asm.ducks.IPlayerControllerMP;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.player.fastbreak.mode.MineMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.util.ArrayList;
import java.util.Collection;

//TODO: maybe auto head place?
//TODO: alternative swap breaks after a CPacketHeldItemChange is sent, find a way to fix it
public class FastBreak extends Module {

    protected final EnumProperty<ListEnum> whitelist =
            new EnumProperty<>(
                    ListEnum.ANY,
                    ListEnum.SELECTION_ALIAS,
                    ListEnum.SELECTION_DESCRIPTION);

    protected final EnumProperty<RotationsEnum> rotation =
            new EnumProperty<>(RotationsEnum.NONE,
                    RotationsEnum.ALIASES,
                    RotationsEnum.DESCRIPTION
            );

    protected final EnumProperty<MineMode> mode =
            new EnumProperty<>(
                    MineMode.PACKET,
                    new String[]{"Mode", "instantmine", "packetmine", "type", "method"},
                    "Packet: - Breaks blocks the same as normal mining " +
                            "/ Instant: - Breaks the first block normally then instantly rebreaks that position until we select another."
            );

    protected final EnumProperty<Swap> swap =
            new EnumProperty<>(
                    Swap.NONE,
                    new String[]{"Swap", "Switch", "swit"},
                    "Silent: - Uses just a packet to switch " +
                            "/ Alternative: - Uses a different packet to switch using area 51 technology."
            );

    protected final Property<Boolean> fast =
            new Property<>(
                    false,
                    new String[]{"Fast", "fas", "fastrebreak"},
                    "Calculates the block damage even if its air so we instantly rebreak it without getting flagged."
            );

    protected final Property<Boolean> strict =
            new Property<>(
                    false,
                    new String[]{"Strict", "bypass"},
                    "Wont mine blocks unless we are on the ground, also a different block damage calculator."
            );

    protected final Property<Boolean> auto =
            new Property<>(
                    true,
                    new String[]{"AutoBreak", "Auto", "A"},
                    "Automatically breaks the block instead of whenever we click it."
            );

    protected final NumberProperty<Float> range =
            new NumberProperty<>(
                    4.5f, 0.1f, 6.0f, 0.1f,
                    new String[]{"Range", "Distance", "rang"},
                    "How far we have to be from the block to cancel breaking it."
            );

    protected final NumberProperty<Float> lineWidth =
            new NumberProperty<>(
                    1.4f, 1f, 4f, 0.1f,
                    new String[]{"LineWidth", "WireWidth", "ww", "lw"},
                    "Thickness of the outline."
            );

    protected final NumberProperty<Integer> alpha =
            new NumberProperty<>(
                    50, 0, 255,
                    new String[]{"BoxAlpha", "boxa", "alpha"},
                    "Opacity of the box."
            );

    protected final NumberProperty<Integer> lineAlpha =
            new NumberProperty<>(
                    125, 0, 255,
                    new String[]{"LineAlpha", "linea", "linalpha"},
                    "Opacity of the outline."
            );

    protected final Property<Boolean> debug =
            new Property<>(
                    false,
                    new String[]{"Debug", "debugger"},
                    "dgub."
            );

    protected final BlockList blockList = new BlockList(ListEnum.BLOCKS_LIST_ALIAS);

    protected final StopWatch timer = new StopWatch();
    protected final StopWatch rotationTimer = new StopWatch();
    protected final StopWatch retryTimer = new StopWatch();
    protected final StopWatch crystalTimer = new StopWatch();
    protected final StopWatch pingTimer = new StopWatch();

    protected final float[] damages = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    protected float maxDamage;

    protected int retries;
    protected int clicks;

    protected boolean shouldAbort;
    protected boolean swapped;
    protected boolean canBreak;
    protected boolean executed;

    protected BlockPos pos;
    protected IBlockState state;
    protected EnumFacing direction;

    protected BlockPos crystalPos;
    protected boolean crystalSwapped;
    protected boolean crystalAttack;
    protected int crystalRetries;
    protected int crystalID;

    public FastBreak() {
        super("FastBreak", new String[]{"FastBreak", "SpeedyGonzales", "SpeedMine", "FastDestroy", "FastMine", "SpeedDestroy", "SpeedBreak"}, "Destroys blocks faster.", Category.PLAYER);
        this.offerProperties(whitelist, rotation, mode, swap, fast, strict, auto, range, lineAlpha, lineWidth, alpha, blockList, debug);
        this.offerListeners(new ListenerDamage(this), new ListenerRender(this), new ListenerSwap(this), new ListenerUpdate(this), new ListenerClickBlock(this),
                new ListenerMotion(this), new ListenerBlockChange(this), new ListenerLogout(this), new ListenerDigging(this), new ListenerSpawnObject(this));
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public String getSuffix() {
        return "" + MathUtil.round(maxDamage, 1);
    }

    public BlockPos getPos() {
        return pos;
    }

    protected Block getBlock() {
        return mc.world.getBlockState(pos).getBlock();
    }

    protected void abortCurrentPos() {
        PacketUtil.swing();
        PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, direction));
        PacketUtil.swing();
        ((IPlayerControllerMP) mc.playerController).setIsHittingBlock(false);
        ((IPlayerControllerMP) mc.playerController).setCurBlockDamageMP(0.0f);
        mc.world.sendBlockBreakProgress(mc.player.getEntityId(), pos, -1);
        reset();
    }

    public void sendPackets() { //test
        final ArrayList<Packet<?>> packets = new ArrayList<>();

        final CPacketAnimation animation = new CPacketAnimation(EnumHand.MAIN_HAND);

        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, direction));
        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, direction));
        packets.add(animation);
        packets.add(animation);
        packets.add(animation);
        packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, direction));
        packets.add(animation);

        if (fast.getValue()) {
            packets.add(animation);
            packets.add(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, direction));
            packets.add(animation);
        }

        for (Packet<?> packet : packets) {
            PacketUtil.send(packet);
        }
    }

    protected void reset() {
        pos = null;
        crystalPos = null;
        direction = null;
        maxDamage = 0.0f;
        retries = 0;
        clicks = 0;
        canBreak = false;
        executed = false;
        swapped = false;
        crystalSwapped = false;
        crystalAttack = false;
        crystalID = -1;
        crystalRetries = 0;

        for (int i = 0; i < 9; i++) {
            damages[i] = 0.0f;
        }
    }

    protected void checkRetry() {
        switch (mode.getValue()) {
            case PACKET: {
                softReset(true);
                clicks = 0;
                break;
            }
            case INSTANT: {
                if (retries >= 3) {
                    softReset(true);
                    retries = 0;
                }
                retryTimer.reset();
                retries++;
                break;
            }
        }
        executed = false;
        resetSwap();
    }

    protected void softReset(boolean full) {
        if (debug.getValue()) {
            Logger.getLogger().log(TextColor.AQUA + "Soft reset", false);
        }

        if (full) {
            PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, direction));
        }

        maxDamage = 0.0f;
        for (int i = 0; i < 9; i++) {
            damages[i] = 0.0f;
        }

        retries = 0;
        executed = false;
    }

    protected void updateDamages() {
        maxDamage = 0.0f;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            float damage;
            if (strict.getValue() && getBlock() == Blocks.AIR && state != null) {
                damage = 0.0F;
            } else if (getBlock() == Blocks.AIR && state != null) {
                damage = MineUtil.getDamage(state, stack, mc.player.onGround);
            } else {
                damage = MineUtil.getDamage(stack, pos, mc.player.onGround);
            }

            damage *= Managers.TPS.getFactor();

            damages[i] = MathUtil.clamp(damages[i] + damage, 0.0f, 1.0f);

            if (damages[i] > maxDamage) {
                maxDamage = damages[i];
            }
        }
    }

    protected void tryBreak(int pickSlot) {
        int lastSlot = mc.player.inventory.currentItem;
        float[] rotations = RotationUtil.getRotations(pos);
        EntityPlayer target = getPlacePlayer(pos);

        if (target != null) {
            BlockPos pos = getCrystalPos(this.pos);

            if (pos != null && (mc.player.getDistanceSq(pos) <= MathUtil.square(range.getValue())) && BlockUtil.canPlaceCrystal(pos, false)) {

                final RayTraceResult result = RaytraceUtil.getRayTraceResult(rotations[0], rotations[1]);

                if (mc.player.getHeldItemOffhand() != ItemStack.EMPTY && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL && !crystalSwapped) {
                    crystalAttack = true;

                    ItemUtil.syncItem();

                    PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(pos, result.sideHit, EnumHand.OFF_HAND, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z));
                    PacketUtil.send(new CPacketAnimation(EnumHand.OFF_HAND));

                    crystalSwapped = true;

                } else {
                    final int crystalSlot = ItemUtil.findHotbarItem(Items.END_CRYSTAL);
                    if (crystalSlot != -1 && !crystalSwapped) {
                        crystalAttack = true;

                        ItemStack oldItemC = mc.player.getHeldItemMainhand();

                        SwitchUtil.doSwitch(swap.getValue(), crystalSlot);

                        ItemStack newItemC = mc.player.getHeldItemMainhand();

                        PacketUtil.send(new CPacketPlayerTryUseItemOnBlock(pos, result.sideHit, EnumHand.MAIN_HAND, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z));
                        PacketUtil.swing();

                        if (swap.getValue() == Swap.ALTERNATIVE) {
                            if (lastSlot != pickSlot) {
                                SwitchUtil.doSwitch(swap.getValue(), crystalSlot);
                            } else {
                                short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                                ItemStack fakeStackC = new ItemStack(Items.END_CRYSTAL, 64);
                                int slotC = ItemUtil.hotbarToInventory(crystalSlot);
                                int oldSlotC = ItemUtil.hotbarToInventory(lastSlot);
                                Slot currentSlotC = mc.player.inventoryContainer.inventorySlots.get(oldSlotC);
                                Slot swapSlotC = mc.player.inventoryContainer.inventorySlots.get(slotC);
                                PacketUtil.send(new CPacketClickWindow(0, slotC, mc.player.inventory.currentItem, ClickType.SWAP, fakeStackC, id));
                                currentSlotC.putStack(oldItemC);
                                swapSlotC.putStack(newItemC);
                            }
                        } else if (lastSlot == pickSlot) {
                            SwitchUtil.doSwitch(swap.getValue(), lastSlot);
                        }

                        crystalSwapped = true;
                    }
                }
            }
        }

        if ((swap.getValue() != Swap.NONE && lastSlot != pickSlot && !swapped) || (lastSlot == pickSlot && !swapped)) {
            if (getBlock() != Blocks.AIR) {
                if (mode.getValue() == MineMode.PACKET) {
                    rotationTimer.reset();
                }

                if (rotation.getValue() == RotationsEnum.PACKET) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                }

                ItemStack oldItem = mc.player.getHeldItemMainhand();

                PacketUtil.swing();
                SwitchUtil.doSwitch(swap.getValue(), pickSlot);

                ItemStack newItem = mc.player.getHeldItemMainhand();

                PacketUtil.swing();
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, direction));
                PacketUtil.swing();
                PacketUtil.swing();
                PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, direction));
                PacketUtil.swing();
                PacketUtil.swing();

                if (mode.getValue() == MineMode.PACKET) {
                    PacketUtil.swing();
                    PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, direction));
                    PacketUtil.swing();

                    if (fast.getValue()) {
                        PacketUtil.swing();
                        PacketUtil.send(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, direction));
                        PacketUtil.swing();
                    }
                }

                if (swap.getValue() != Swap.ALTERNATIVE) {
                    SwitchUtil.doSwitch(swap.getValue(), lastSlot);
                } else {
                    short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                    ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                    int slot = ItemUtil.hotbarToInventory(pickSlot);
                    int oldSlot = ItemUtil.hotbarToInventory(lastSlot);
                    Slot currentSlot = mc.player.inventoryContainer.inventorySlots.get(oldSlot);
                    Slot swapSlot = mc.player.inventoryContainer.inventorySlots.get(slot);
                    PacketUtil.send(new CPacketClickWindow(0, slot, mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                    currentSlot.putStack(oldItem);
                    swapSlot.putStack(newItem);
                }

                swapped = true;
            }

            if (mode.getValue() == MineMode.PACKET) {
                softReset(false);
            }

        }
    }

    private EntityPlayer getPlacePlayer(BlockPos pos) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (Managers.FRIEND.isFriend(player) || player == mc.player || EntityUtil.isOnBurrow(player) || EntityUtil.isDead(player)) continue;
            final BlockPos playerPos = PositionUtil.getPosition(player);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (playerPos.offset(facing).equals(pos) || playerPos.up().offset(facing).equals(pos)) {
                    return player;
                }
            }
            if (playerPos.offset(EnumFacing.UP).offset(EnumFacing.UP).equals(pos)) {
                return player;
            }
        }

        return null;
    }

    private BlockPos getCrystalPos(BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.OBSIDIAN) {
            return pos;
        }
        return null;
    }

    protected void resetSwap() {
        swapped = false;
        crystalSwapped = false;
    }

    protected int getPingDelay() {
        if (mc.player != null) {
            if (NetworkUtil.getLatencyNoSpoof() < 25) {
                return 150;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 50) {
                return 75;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 100) {
                return 25;
            }
            if (NetworkUtil.getLatencyNoSpoof() < 200) {
                return 0;
            }
        }
        return 25;
    }

    public boolean isBlockValid(Block block) {
        if (block == null) {
            return false;
        }

        if (block instanceof BlockAir) { //fix for instant?
            return true;
        }

        if (whitelist.getValue() == ListEnum.ANY) {
            return true;
        }

        if (whitelist.getValue() == ListEnum.WHITELIST) {
            return getList().contains(block);
        }

        return !getList().contains(block);
    }

    public Collection<Block> getList() {
        return blockList.getValue();
    }

}
