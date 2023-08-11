package me.chachoox.lithium.api.module;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.holefill.HoleFill;
import me.chachoox.lithium.impl.modules.combat.instantweb.InstantWeb;
import me.chachoox.lithium.impl.modules.other.blocks.BlocksManager;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BlockPlaceModule extends Module {

    protected final Property<Boolean> rotation =
            new Property<>(false,
                    new String[]{"Rotation", "rotate", "rots"},
                    "Rotates to look at the block being placed."
            );

    protected final Property<Boolean> swing =
            new Property<>(false,
                    new String[]{"Swing", "swingarm"},
                    "Swings your arm when placing blocks."
            );

    protected final NumberProperty<Integer> delay =
            new NumberProperty<>(
                    3, 0, 10,
                    new String[]{"Delay", "Interval"},
                    "Delay between placing blocks."
            );

    protected final NumberProperty<Integer> blocks =
            new NumberProperty<>(2, 1, 10,
                    new String[]{"Blocks", "BlocksPerTick", "Blocks/Tick"},
                    "The amount of blocks we place per tick."
            );

    protected final Property<Boolean> strict =
            new Property<>(false,
                    new String[]{"StrictDirection", "strict"},
                    "Wont place blocks through walls."
            );

    protected final NumberProperty<Float> strictRange =
            new NumberProperty<>(3.0F, 1.0F, 5.0F, 0.1F,
                    new String[]{"StrictRange", "wallrange"},
                    "How far will place blocks through walls."
            );

    protected final Property<Boolean> altSwap =
            new Property<>(false,
                    new String[]{"AltSwap", "AlternativeSwap", "CooldownBypass"},
                    "Uses an alternative type of swap that bypass some anticheats (2b2tpvp main)"
            );

    protected final Property<Boolean> preferWebs =
            new Property<>(false,
                    new String[]{"OnlyWebs", "PreferWebs", "UseWebs"},
                    "Prefers webs when filling holes."
            );


    public BlockPlaceModule(String label, String[] aliases, String description, Category category) {
        super(label, aliases, description, category);
        this.offerProperties(rotation, swing, delay, blocks, strict, strictRange);
        if (this instanceof HoleFill) {
            this.offerProperties(altSwap, preferWebs);
        }
        this.offerListeners(listenerDeath(this), listenerDisconnect(this));
    }

    private final List<Packet<?>> packets = new ArrayList<>();
    private final StopWatch timer = new StopWatch();
    private float[] rotations;
    public double enablePosY;
    private int blocksPlaced = 0;
    private int slot = -1;

    @Override
    public String getSuffix() {
        return "" + blocksPlaced;
    }

    @Override
    public void onEnable() {
        packets.clear();
        blocksPlaced = 0;
        clear();

        if (this.isNull()) {
            this.disable();
            return;
        }

        enablePosY = mc.player.posY;
    }

    public void clear() {
        //Implemented by module
    }

    public void onPreEvent(List<BlockPos> blocks, MotionUpdateEvent event) {
        if (event.getStage() == Stage.PRE) {
            blocksPlaced = 0;
            placeBlocks(blocks);
            if (rotations != null) {
                Managers.ROTATION.setRotations(rotations[0], rotations[1]);
                rotations = null;
            }
            execute();
        }
    }

    public void placeBlocks(List<BlockPos> blockList) {
        if (blockList == null || blockList.isEmpty()) {
            return;
        }

        for (BlockPos pos : blockList) {
            placeBlock(pos);
        }
    }

    public void placeBlock(BlockPos pos) {
        getSlot(BlocksManager.get().placeEnderChests(this));
        if (slot == -1) {
            return;
        }

        if (!timer.passed(delay.getValue() * 50)) {
            return;
        }

        EnumFacing facing = BlockUtil.getFacing(pos);

        //TODO: raytracer
        /*
        if (facing == null) {
            BlockPos helpingPos;
            for (EnumFacing side : EnumFacing.values()) {
                helpingPos = pos.offset(side);
                EnumFacing helpingFacing = BlockUtil.getFacing(helpingPos);
                if (helpingFacing != null) {
                    facing = side;
                    placeBlock(helpingPos.offset(helpingFacing), helpingFacing.getOpposite());
                    break;
                }
            }
        }
        */

        if (facing == null) {
            return;
        }

        if (blocksPlaced >= blocks.getValue()) {
            return;
        }

        if (mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) {
            return;
        }

        if (crystalCheck(pos) && !(this instanceof HoleFill || this instanceof InstantWeb)) {
            timer.reset();
            return;
        }

        if (canPlaceBlock(pos, strict.getValue())) {
            placeBlock(pos.offset(facing), facing.getOpposite());
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing facing) {
        float[] rots = RotationUtil.getRotations(pos, facing);
        RayTraceResult result = RaytraceUtil.getRayTraceResult(rots[0], rots[1]);
        placeBlock(pos, facing, rots, result.hitVec);
    }

    private void placeBlock(BlockPos on, EnumFacing facing, float[] helpingRotations, Vec3d hitVec) {
        if (rotation.getValue()) {
            if (rotations == null) {
                rotations = helpingRotations;
            }
            packets.add(new CPacketPlayer.Rotation(helpingRotations[0], helpingRotations[1], mc.player.onGround));
        }

        float[] hitRots = RaytraceUtil.hitVecToPlaceVec(on, hitVec);
        packets.add(new CPacketPlayerTryUseItemOnBlock(on, facing, EnumHand.MAIN_HAND, hitRots[0], hitRots[1], hitRots[2]));

        if (swing.getValue()) {
            packets.add(new CPacketAnimation(EnumHand.MAIN_HAND));
        }

        blocksPlaced++;
    }

    private void execute() {
        if (!packets.isEmpty()) {
            int lastSlot = mc.player.inventory.currentItem;

            ItemStack oldItem = mc.player.getHeldItemMainhand();

            if (altSwap.getValue() && (this instanceof HoleFill)) {
                ItemUtil.switchToAlt(slot);
            } else {
                ItemUtil.switchTo(slot);
            }

            ItemStack newItem = mc.player.getHeldItemMainhand();

            PacketUtil.sneak(true);

            if (swing.getValue()) { //this is only for the animation
                EntityUtil.swingClient();
            }

            packets.forEach(PacketUtil::send);

            packets.clear();
            timer.reset();

            PacketUtil.sneak(false);

            if (altSwap.getValue() && (this instanceof HoleFill)) {
                short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                int newSlot = ItemUtil.hotbarToInventory(slot);
                int altSlot = ItemUtil.hotbarToInventory(lastSlot);
                Slot currentSlot = mc.player.inventoryContainer.inventorySlots.get(altSlot);
                Slot swapSlot = mc.player.inventoryContainer.inventorySlots.get(newSlot);
                PacketUtil.send(new CPacketClickWindow(0, newSlot, mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                currentSlot.putStack(oldItem);
                swapSlot.putStack(newItem);
            } else {
                ItemUtil.switchTo(lastSlot);
            }
        }
    }

    private boolean crystalCheck(BlockPos pos) {
        CPacketUseEntity attackPacket = null;
        float currentDmg = Float.MAX_VALUE;
        float[] angles = null;
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
            if (entity == null || !EntityUtil.isDead(entity)) {
                continue;
            }

            if (entity instanceof EntityEnderCrystal) {
                float damage = DamageUtil.calculate(entity, mc.player);
                if (damage < currentDmg) {
                    currentDmg = damage;
                    angles = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
                    attackPacket = new CPacketUseEntity(entity);
                }
            }
        }

        if (attackPacket == null) {
            return false;
        }

        int weaknessSlot = -1;
        int oldSlot = mc.player.inventory.currentItem;
        if (!DamageUtil.canBreakWeakness(true)) {
            if ((weaknessSlot = DamageUtil.findAntiWeakness()) == -1) {
                return true;
            }
        }

        if (weaknessSlot != -1) {
            if (rotation.getValue()) {
                if (rotations == null) {
                    rotations = angles;
                }

                PacketUtil.send(new CPacketPlayer.Rotation(angles[0], angles[1], mc.player.onGround));
            }

            ItemUtil.switchTo(weaknessSlot);

            PacketUtil.send(attackPacket);

            if (swing.getValue()) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }

            ItemUtil.switchTo(oldSlot);

            return false;
        }

        if (rotation.getValue()) {
            if (rotations == null) {
                rotations = angles;
            }

            packets.add(new CPacketPlayer.Rotation(angles[0], angles[1], mc.player.onGround));
        }

        packets.add(attackPacket);

        if (swing.getValue()) {
            packets.add(new CPacketAnimation(EnumHand.MAIN_HAND));
        }

        return false;
    }

    private boolean canPlaceBlock(BlockPos pos, boolean strict) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return false;
        }

        if (!(this instanceof InstantWeb)) {
            for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity instanceof EntityEnderCrystal)
                    continue;
                return false;
            }
        }

        for (EnumFacing side : getPlacableFacings(pos, strict)) {
            if (!canClick(pos.offset(side))) continue;
            return true;
        }

        return false;
    }

    private boolean canClick(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().canCollideCheck(mc.world.getBlockState(pos), false);
    }

    private List<EnumFacing> getPlacableFacings(BlockPos pos, boolean strict) {
        ArrayList<EnumFacing> validFacings = new ArrayList<>();
        for (EnumFacing side : EnumFacing.values()) {
            if (strict && mc.player.getDistanceSq(pos) > MathUtil.square(strictRange.getValue())) {
                Vec3d testVec = new Vec3d(pos).add(0.5, 0.5, 0.5).add(new Vec3d(side.getDirectionVec()).scale(0.5)); //TODO: this can be more accurate
                RayTraceResult result = mc.world.rayTraceBlocks(mc.player.getPositionEyes(1F), testVec);
                if (result != null && result.typeOfHit != RayTraceResult.Type.MISS) {
                    continue;
                }
            }
            BlockPos neighbour = pos.offset(side);
            IBlockState blockState = mc.world.getBlockState(neighbour);
            if ((!blockState.getBlock().canCollideCheck(blockState, false) || blockState.getMaterial().isReplaceable())) {
                continue;
            }

            validFacings.add(side);
        }
        return validFacings;
    }

    private void getSlot(boolean echest) {
        if (this instanceof HoleFill && preferWebs.getValue() || this instanceof InstantWeb) {
            this.slot = ItemUtil.getBlockFromHotbar(Blocks.WEB);
        } else {
            this.slot = ItemUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
            if (this.slot == -1 && echest) {
                this.slot = ItemUtil.getBlockFromHotbar(Blocks.ENDER_CHEST);
            }
        }
    }

    private Listener<?> listenerDeath(Module module) {
        return new Listener<DeathEvent>(DeathEvent.class) {
            @Override
            public void call(DeathEvent event) {
                if (event.getEntity() != null && event.getEntity().equals(mc.player) && BlocksManager.get().disableOnDeath()) {
                    mc.addScheduledTask(module::disable);
                }
            }
        };
    }

    private Listener<?> listenerDisconnect(Module module) {
        return new Listener<DisconnectEvent>(DisconnectEvent.class) {
            @Override
            public void call(DisconnectEvent event) {
                if (BlocksManager.get().disableOnDisconnect()) {
                    mc.addScheduledTask(module::disable);
                }
            }
        };
    }
}
