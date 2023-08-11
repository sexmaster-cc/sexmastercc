package me.chachoox.lithium.impl.modules.combat.autocrystal;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.blocks.BlockUtil;
import me.chachoox.lithium.api.util.blocks.MineUtil;
import me.chachoox.lithium.api.util.entity.CombatUtil;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.MathUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.api.util.rotation.raytrace.RaytraceUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.Swap;
import me.chachoox.lithium.impl.modules.combat.autocrystal.mode.YawStep;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class AutoCrystal extends Module {

    /**
     * ********************* ANTICHEATS **********************
     **/

    protected final Property<Boolean> multiTask =
            new Property<>(
                    false,
                    new String[]{"MultiTask", "PauseEat"},
                    "Stops autocrystal if we are eating."
            );

    protected final Property<Boolean> whileMining =
            new Property<>(
                    false,
                    new String[]{"WhileMining", "MiningPause"},
                    "Stops autocrystal if we are mining."
            );
    /**
     * ********************* ROTATIONS **********************
     **/

    protected final Property<Boolean> rotation =
            new Property<>(
                    false,
                    new String[]{"Rotate", "rots", "r", "LOOK"},
                    "Rotates to crystals when we are placing / breaking them."
            );

    protected final EnumProperty<YawStep> yawStep =
            new EnumProperty<>(
                    YawStep.FULL,
                    new String[]{"YawStep", "Yaw", "Strict"},
                    "Off: - Wont use YawStep / Semi: - Only uses YawStep if yaw is below the first angle / Full: - Always uses YawStep."
            );

    protected final NumberProperty<Integer> yawStepThreshold =
            new NumberProperty<>(
                    25, 0, 180,
                    new String[]{"YawStepThreshold", "YawThreshold"},
                    "How far we have to look down before activating yawstep."
            );

    /**
     * ********************* RAYTRACING **********************
     **/

    protected final Property<Boolean> rayTrace =
            new Property<>(
                    false,
                    new String[]{"RayTrace", "RayTracing", "ThroughWalls"},
                    "Wont break / place crystals through walls."
            );

    /**
     * ********************* BREAKING **********************
     **/

    protected final Property<Boolean> attack =
            new Property<>(
                    true,
                    new String[]{"Break", "Attack", "Destroy", "RAPE"},
                    "Attacks crystals when we are able to."
            );

    protected final Property<Boolean> breakRangeEye =
            new Property<>(
                    false,
                    new String[]{"BreakRangeEye", "EyeBreak"},
                    "Uses crystal eye range instead of just the crystal range."
            );

    protected final NumberProperty<Float> breakRange =
            new NumberProperty<>(
                    5.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"BreakRange", "AttackRange", "RapeRange"},
                    "How far we have to be from a crystal to attack it."
            );

    protected final NumberProperty<Float> breakWallRange =
            new NumberProperty<>(
                    3.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"BreakWallRange", "AttackWallRange"},
                    "How far we have to be from a crystal to attack it."
            );

    protected final NumberProperty<Integer> existed =
            new NumberProperty<>(3, 0, 10,
                    new String[]{"TicksExisted", "Existed"},
                    "How long the crystal has to of existed for (in ticks) before we attack it."
            );

    protected final Property<Boolean> boost =
            new Property<>(
                    true,
                    new String[]{"Boost", "IdPredict", "Fast", "BreakBoost"},
                    "Predicts ids of crystals safely which boost the break speed."
            );

    protected final NumberProperty<Integer> breakDelay =
            new NumberProperty<>(80, 0, 500,
                    new String[]{"BreakDelay", "AttackDelay", "DestroyDelay", "RapeDelay"},
                    "Delay interval for attacking crystals."
            );

    protected final Property<Boolean> antiWeakness =
            new Property<>(
                    true,
                    new String[]{"AntiWeakness", "aw", "NoWeakness"},
                    "Switches to a sword using the same switching method as the \"Switch\" property values."
            );

    protected final Property<Boolean> switchBack =
            new Property<>(
                    false,
                    new String[]{"SwitchBack", "Swapback"},
                    "Switches back to the crystal after hitting the crystal with a sword."
            );

    /**
     * ********************* PLACEMENT **********************
     **/

    protected final Property<Boolean> place =
            new Property<>(
                    false,
                    new String[]{"Place", "Placement"},
                    "Places crystals."
            );

    protected final Property<Boolean> secondCheck =
            new Property<>(
                    true,
                    new String[]{"SecondPlace", "Semi1.13", "CCPlace"},
                    "Tries to place on semi 1.13 placements."
            );

    protected final Property<Boolean> blockDestruction =
            new Property<>(
                    true,
                    new String[]{"BlockDestruction", "ThroughTerrain"},
                    "Ignores terrain that can be exploded and calculates through it."
            );

    protected final NumberProperty<Integer> placeDelay =
            new NumberProperty<>(
                    50, 0, 500,
                    new String[]{"PlaceDelay"},
                    "Delay interval for placing crystals."
            );

    protected final NumberProperty<Float> placeRange =
            new NumberProperty<>(
                    5.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"PlaceRange", "PlacingRange"},
                    "How far we have to be from a crystal to attack it."
            );

    protected final NumberProperty<Float> placeWallRange =
            new NumberProperty<>(
                    3.0f, 1.0f, 6.0f, 0.1f,
                    new String[]{"PlaceWallRange", "PlacingWallRange"},
                    "How far we have to be from a crystal to attack it."
            );

    protected final NumberProperty<Integer> facePlaceHp =
            new NumberProperty<>(
                    8, 0, 36,
                    new String[]{"FacePlace", "FacePlaceHp", "FacePlaceHealth"},
                    "How low an enemy has to be to faceplace."
            );

    protected final NumberProperty<Float> minDamage =
            new NumberProperty<>(
                    4f, 1f, 20f, 0.1f,
                    new String[]{"MinDamage", "MinDmg"},
                    "Minimum damage allowed for a crystal to do to the target."
            );

    protected final NumberProperty<Integer> maxSelfDamage =
            new NumberProperty<>(
                    8, 1, 36,
                    new String[]{"MaxSelfDamage", "MaxSelfDmg"},
                    "Maximum damage allowed to do to ourself."
            );

    protected final NumberProperty<Float> targetRange =
            new NumberProperty<>(
                    9f, 1f, 15f, 0.1f,
                    new String[]{"TargetRange", "TargetDistance"},
                    "How close we have to be to someone for them to be a target."
            );
    protected final NumberProperty<Float> lethalMult =
            new NumberProperty<>(
                    3f, 0f, 6f, 0.1f,
                    new String[]{"LethalMult", "LethalMultiplier"},
                    "Multipler for absoprtion hearts."
            );
    protected final Property<Boolean> armorBreaker =
            new Property<>(
                    true,
                    new String[]{"ArmorBreaker", "ArmorDestroy"},
                    "If we want to faceplace people based off their armor percentage or not."
            );

    protected final NumberProperty<Float> armorScale =
            new NumberProperty<>(
                    15f, 0f, 100f, 0.1f,
                    new String[]{"ArmorScale", "ArmorLevel", "Armor%"},
                    "How low one of the targets armor pieces has to be to faceplace."
            );

    /**
     * ********************* SWITCHING **********************
     **/

    protected final EnumProperty<Swap> swap =
            new EnumProperty<>(
                    Swap.NONE,
                    new String[]{"Switch", "AutoSwitch", "Swap"},
                    "How we are going to switch to the crystal."
            );

    protected final NumberProperty<Float> switchDelay =
            new NumberProperty<>(
                    0.0f, 0.0f, 10.0f, 0.1f,
                    new String[]{"SwitchDelay", "SwapDelay"},
                    "How long we have to wait before switching to the crystal."
            );

    /**
     * ********************* DEBUGGING **********************
     */

    protected final Property<Boolean> debugAttack =
            new Property<>(
                    false,
                    new String[]{"DebugAttack"},
                    ""
            );

    protected final Property<Boolean> debugPlace =
            new Property<>(
                    false,
                    new String[]{"DebugPlace"},
                    ""
            );

    protected final Set<BlockPos> placeSet = new HashSet<>();
    protected final Map<Integer, Integer> attackMap = new HashMap<>();

    protected final StopWatch clearTimer = new StopWatch();
    protected final StopWatch breakTimer = new StopWatch();
    protected final StopWatch placeTimer = new StopWatch();
    protected final StopWatch switchTimer = new StopWatch();

    protected BlockPos renderPos;
    protected EntityPlayer target;

    protected int predictedId = -1, ticks;
    protected boolean offhand;
    protected float damage, actualArmorScale;

    public AutoCrystal() {
        super("AutoCrystal", new String[]{"AutoCrystal", "CrystalNuker", "CrystalNuke", "CrystalAura", "AutoCrystaler", "CA"}, "Automatically breaks/places crystals.", Category.COMBAT);
        this.offerProperties(
                multiTask, whileMining, rotation, yawStep, yawStepThreshold, rayTrace, attack, breakRangeEye, breakRange, breakWallRange, existed, boost, breakDelay, antiWeakness,
                switchBack, place, secondCheck, blockDestruction, placeDelay, placeRange, placeWallRange, facePlaceHp, minDamage, maxSelfDamage, targetRange, lethalMult, armorBreaker, armorScale,
                swap, switchDelay, debugAttack, debugPlace
        );
        this.offerListeners(new ListenerMotion(this), new ListenerSound(this), new ListenerSpawnObject(this),
                new ListenerRender(this), new ListenerDestroy(this), new ListenerExplosion(this));
        this.armorScale.addObserver(event -> actualArmorScale = armorScale.getValue());
    }

    @Override
    public String getSuffix() {
        if (target != null) {
            return MathUtil.round(damage, 2) + "";
        }
        return null;
    }

    @Override
    public void onEnable() {
        target = null;
        attackMap.clear();
        placeSet.clear();
        predictedId = -1;
        renderPos = null;
    }

    protected void doAutoCrystal() {
        if (isNull()) {
            return;
        }

        if (multiTask.getValue()) {
            if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItemUseAction().equals(EnumAction.EAT) || mc.player.getActiveItemStack().getItemUseAction().equals(EnumAction.DRINK)) {
                return;
            }
        }

        if (whileMining.getValue()) {
            if (ItemUtil.isHolding(ItemTool.class) && mc.playerController.getIsHittingBlock() && MineUtil.canBreak(mc.objectMouseOver.getBlockPos()) && !mc.world.isAirBlock(mc.objectMouseOver.getBlockPos())) {
                return;
            }
        }

        target = CombatUtil.getTarget(targetRange.getValue());

        if (ticks < 40) {
            ticks++;
        } else {
            ticks = 0;
            attackMap.clear();
        }

        if (clearTimer.passed(500)) {
            placeSet.clear();
            attackMap.clear();
            predictedId = -1;
            renderPos = null;
            clearTimer.reset();
        }

        offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;

        doBreak();
        doPlace();
    }

    protected void doBreak() {
        if (attack.getValue()) {
            if (target == null) {
                return;
            }

            float maxDamage = 0;

            Entity maxCrystal = null;
            float minDmg = isArmorUnderPercent() ? EntityUtil.getHealth(target) < facePlaceHp.getValue() ? 2 : minDamage.getValue() : 2.5f;
            for (Entity crystal : mc.world.loadedEntityList) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (crystal.isDead) {
                        continue;
                    }

                    if (breakRangeEye.getValue()) {
                        if (mc.player.getDistance(crystal.posX, crystal.posY + crystal.getEyeHeight(), crystal.posZ) > (RaytraceUtil.canBeSeen(crystal, mc.player) ? breakRange.getValue() : breakWallRange.getValue())) {
                            continue;
                        }
                    }

                    if (!breakRangeEye.getValue() && mc.player.getDistance(crystal) > (RaytraceUtil.canBeSeen(crystal, mc.player) ? breakRange.getValue() : breakWallRange.getValue())) {
                        continue;
                    }

                    if (existed.getValue() != 0) {
                        if (crystal.ticksExisted < existed.getValue()) {
                            continue;
                        }
                    }

                    if (boost.getValue() && existed.getValue() != 0 && crystal.ticksExisted < 0.2) {
                        continue;
                    }

                    if (rayTrace.getValue() && !RaytraceUtil.canBlockBeSeen(mc.player, crystal.getPosition(), true)) {
                        return;
                    }

                    if (attackMap.containsKey(crystal.getEntityId()) && attackMap.get(crystal.getEntityId()) > 5) {
                        continue;
                    }

                    float targetDamage = EntityUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, target, blockDestruction.getValue());
                    if (targetDamage > minDmg || targetDamage * lethalMult.getValue() > target.getHealth() + target.getAbsorptionAmount()) {
                        float selfDamage = !mc.player.capabilities.isCreativeMode ? EntityUtil.calculate(crystal.posX, crystal.posY, crystal.posZ, mc.player, blockDestruction.getValue()) : 0;
                        if (selfDamage > maxSelfDamage.getValue() || selfDamage + 0.5F >= EntityUtil.getHealth(mc.player)) {
                            continue;
                        }

                        if (targetDamage > maxDamage) {
                            maxDamage = targetDamage;
                            damage = maxDamage;
                            maxCrystal = crystal;
                        }
                    }
                }
            }

            if (maxCrystal != null) {
                int lastSlot = -1;
                int swordSlot = DamageUtil.findAntiWeakness();

                if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    boolean shouldSwitch = !mc.player.isPotionActive(MobEffects.STRENGTH) || Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() != 2;

                    if (shouldSwitch && swordSlot != -1) {
                        lastSlot = mc.player.inventory.currentItem;
                        if (swap.getValue() == Swap.ALTERNATIVE) {
                            ItemUtil.switchToAlt(swordSlot);
                        } else {
                            ItemUtil.switchTo(swordSlot);
                        }
                    }
                }

                if (rotation.getValue()) {
                    rotateToEntity(maxCrystal);
                }

                if (debugAttack.getValue()) {
                    Logger.getLogger().log(String.format("Attacking Crystal at %s [Damage = %s]",
                            maxCrystal.getPosition(),
                            EntityUtil.calculate(maxCrystal.posX, maxCrystal.posY, maxCrystal.posZ, target, blockDestruction.getValue())), false);
                }

                PacketUtil.send(new CPacketUseEntity(maxCrystal));
                attackMap.put(maxCrystal.getEntityId(), attackMap.containsKey(maxCrystal.getEntityId()) ? attackMap.get(maxCrystal.getEntityId()) + 1 : 1);
                mc.player.swingArm(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                breakTimer.reset();

                if ((switchBack.getValue() || swap.getValue() == Swap.ALTERNATIVE) && swordSlot != -1) {
                    if (lastSlot == -1) {
                        return;
                    }

                    if (swap.getValue() == Swap.ALTERNATIVE) {
                        ItemUtil.switchToAlt(swordSlot);
                    } else {
                        ItemUtil.switchTo(lastSlot);
                    }
                }
            }
        }
    }

    protected void doPlace() {
        if (place.getValue()) {

            if (target == null) {
                return;
            }

            float maxDamage = 0;
            float minDmg = isArmorUnderPercent() ? EntityUtil.getHealth(target) < facePlaceHp.getValue() ? 2 : minDamage.getValue() : 2.5f;
            BlockPos placePos = null;
            for (BlockPos pos : BlockUtil.getSphere(targetRange.getValue(), true)) {
                if (!BlockUtil.canPlaceCrystal(pos, secondCheck.getValue())) {
                    continue;
                }

                if (BlockUtil.getDistanceSq(pos) > (RaytraceUtil.canBlockBeSeen(mc.player, pos, true) ? MathUtil.square(placeRange.getValue()) : MathUtil.square(placeWallRange.getValue()))) {
                    continue;
                }

                if (rayTrace.getValue() && !RaytraceUtil.canBlockBeSeen(mc.player, pos, true)) {
                    return;
                }

                float targetDamage = !target.capabilities.isCreativeMode ? EntityUtil.calculate(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, target, blockDestruction.getValue()) : 0.0F;

                if (targetDamage < minDmg || targetDamage * lethalMult.getValue() < target.getHealth() + target.getAbsorptionAmount()) {
                    continue;
                }

                float selfDamage = !mc.player.capabilities.isCreativeMode ? EntityUtil.calculate(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, mc.player, blockDestruction.getValue()) : 0.0F;

                if (selfDamage > maxSelfDamage.getValue() || selfDamage + 0.5F >= EntityUtil.getHealth(mc.player)) {
                    continue;
                }

                if (targetDamage > maxDamage) {
                    maxDamage = targetDamage;
                    placePos = pos;
                }
            }

            if (placePos != null) {
                if (placeDelay.getValue() != 0) {
                    if (!placeTimer.passed(placeDelay.getValue())) {
                        return;
                    }
                }

                int crystalSlot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                int oldSlot = mc.player.inventory.currentItem;

                if (crystalSlot == -1 && !offhand) {
                    return;
                }

                ItemStack oldItem = mc.player.getHeldItemMainhand();

                boolean switched = false;

                if (swap.getValue() != Swap.NONE && !offhand && !ItemUtil.isHolding(ItemEndCrystal.class)) {
                    switched = true;
                    switch (swap.getValue()) {
                        case NORMAL: {
                            if (switchTimer.passed(switchDelay.getValue() * 10)) {
                                ItemUtil.switchTo(crystalSlot);
                            }

                            switchTimer.reset();
                            break;
                        }
                        case SILENT: {
                            ItemUtil.switchTo(crystalSlot);
                            break;
                        }
                        case ALTERNATIVE: {
                            ItemUtil.switchToAlt(crystalSlot);
                            break;
                        }
                    }
                }

                if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) && !offhand) {
                    return;
                }

                ItemStack newItem = mc.player.getHeldItemMainhand();

                if (rotation.getValue()) {
                    rotateToPos(placePos);
                }

                if (debugPlace.getValue()) {
                    Logger.getLogger().log(String.format("Placing Crystal at X (%s) Y (%s) Z (%s) [Damage = %s]",
                            placePos.getX(),
                            placePos.getY(),
                            placePos.getZ(),
                            EntityUtil.calculate(placePos.getX(), placePos.getY(), placePos.getZ(), target, blockDestruction.getValue())), false);
                }

                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.5F, 1F, 0.5F));
                placeSet.add(placePos);
                renderPos = placePos;
                placeTimer.reset();

                if (swap.getValue() != Swap.NONE && !offhand && switched) {
                    switch (swap.getValue()) {
                        case SILENT: {
                            ItemUtil.switchTo(oldSlot);
                            break;
                        }
                        case ALTERNATIVE: {//TODO:find way to make it swinginton
                   //         EntityUtil.swingClient();
                            short id = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
                            ItemStack fakeStack = new ItemStack(Items.END_CRYSTAL, 64);
                            int slot = ItemUtil.hotbarToInventory(crystalSlot);
                            int altSlot = ItemUtil.hotbarToInventory(oldSlot);
                            Slot currentSlot = mc.player.inventoryContainer.inventorySlots.get(altSlot);
                            Slot swapSlot = mc.player.inventoryContainer.inventorySlots.get(slot);
                            PacketUtil.send(new CPacketClickWindow(0, slot, mc.player.inventory.currentItem, ClickType.SWAP, fakeStack, id));
                            currentSlot.putStack(oldItem);
                            swapSlot.putStack(newItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void rotateToPos(BlockPos pos) {
        float[] angle = RotationUtil.getRotations(pos, EnumFacing.UP);
        if (!yawStep.getValue().equals(YawStep.OFF) && rotation.getValue()) {
            float yaw = MathHelper.wrapDegrees(Managers.ROTATION.getYaw());
            float angleDifference = angle[0] - yaw;
            if (Math.abs(angleDifference) > 180) {
                float adjust = angleDifference > 0 ? -360 : 360;
                angleDifference += adjust;
            }
            if (Math.abs(angleDifference) > yawStepThreshold.getValue()) {
                if (yawStep.getValue().equals(YawStep.FULL) || (yawStep.getValue().equals(YawStep.SEMI) && angle[0] > yawStepThreshold.getValue())) {
                    int rotationDirection = angleDifference > 0 ? 1 : -1;
                    yaw += yawStepThreshold.getValue() * rotationDirection;
                    Managers.ROTATION.setRotations(yaw, angle[1]);
                }
            }
        }
        Managers.ROTATION.setRotations(angle[0], angle[1]);
    }

    protected void rotateToEntity(Entity entity) {
        float[] angle = RotationUtil.getRotations(entity.posX, entity.posY, entity.posZ);
        if (!yawStep.getValue().equals(YawStep.OFF) && rotation.getValue()) {
            float yaw = MathHelper.wrapDegrees(Managers.ROTATION.getYaw());
            float angleDifference = angle[0] - yaw;
            if (Math.abs(angleDifference) > 180) {
                float adjust = angleDifference > 0 ? -360 : 360;
                angleDifference += adjust;
            }
            if (Math.abs(angleDifference) > yawStepThreshold.getValue()) {
                if (yawStep.getValue().equals(YawStep.FULL) || (yawStep.getValue().equals(YawStep.SEMI) && angle[0] > yawStepThreshold.getValue())) {
                    int rotationDirection = angleDifference > 0 ? 1 : -1;
                    yaw += yawStepThreshold.getValue() * rotationDirection;
                    Managers.ROTATION.setRotations(yaw, angle[1]);
                }
            }
        }
        Managers.ROTATION.setRotations(angle[0], angle[1]);
    }

    private boolean isArmorUnderPercent() {
        if (armorBreaker.getValue()) {
            if (target != null) {
                for (ItemStack armor : target.getArmorInventoryList()) {
                    if (armor != null && !armor.getItem().equals(Items.AIR)) {
                        float armorDurability = ((armor.getMaxDamage() - armor.getItemDamage()) / (float) armor.getMaxDamage()) * 10;
                        if (armorDurability < actualArmorScale) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
