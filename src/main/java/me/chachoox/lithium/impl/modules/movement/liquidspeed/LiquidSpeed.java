package me.chachoox.lithium.impl.modules.movement.liquidspeed;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;

/**
 * @author moneymaker552
 */
public class LiquidSpeed extends Module {

    private final EnumProperty<LiquidDetection> detection =
            new EnumProperty<>(
                    LiquidDetection.STRICT,
                    new String[]{"Detection", "detect", "mode", "type"},
                    "Strict: - Only changes speed if entire body is in liquid / Fast: - Only changes speed if any body part is in liquid."
            );

    private final NumberProperty<Float> waterSpeed =
            new NumberProperty<>(
                    1.0f, 0.1f, 15.0f, 0.1f,
                    new String[]{"WaterSpeed", "BaseSpeed", "WaterSped", "ws"},
                    "Speed for water."
            );

    private final NumberProperty<Float> lavaSpeed =
            new NumberProperty<>(
                    1.0f, 0.1f, 15.0f, 0.1f,
                    new String[]{"LavaSpeed", "LavaSped", "SpeedLava", "ls"},
                    "Speed for lava."
            );

    private final NumberProperty<Float> elytraSpeed =
            new NumberProperty<>(
                    1.0f, 0.1f, 15.0f, 0.1f,
                    new String[]{"ElytraSpeed", "Espeed", "ElySpeed", "es"},
                    "Speed for lava when we are elytra flying."
            );


    private final Property<Boolean> ySpeed =
            new Property<>(
                    true,
                    new String[]{"YSpeed", "YSped", "UpwardsSpeed", "VeritcalSpeed", "DownwardsSpeed"},
                    "Modifies upwards and downwards speed in both liquids."
            );

    private final Property<Boolean> strafe =
            new Property<>(
                    true,
                    new String[]{"Strafe", "straf", "fast"},
                    "Strafes in liquids, use a lower value for speed with this."
            );

    private final Property<Boolean> elytra =
            new Property<>(
                    true,
                    new String[]{"Elytra", "Elytras", "Ely"},
                    "Uses elytra fly in liquids using the elytra speed property."
            );

    private final Property<Boolean> depthStrider =
            new Property<>(
                    true,
                    new String[]{"DepthStrider", "Strider", "NoDepthStrider"},
                    "Wont use speed if we have depth strider boots."
            );

    private final Property<Boolean> cancelSneak =
            new Property<>(
                    true,
                    new String[]{"CancelSneak", "noSneak", "StopSneak"},
                    "Cancels the sneaking server side if we are going downwards in liquid."
            );

    private final StopWatch timer = new StopWatch();

    public LiquidSpeed() {
        super("LiquidSpeed", new String[]{"LiquidSpeed", "LavaSpeed", "WaterSpeed", "FastSwim"}, "Tweaks player speed while in liquids.", Category.MOVEMENT);
        this.offerProperties(detection, waterSpeed, lavaSpeed, elytra, strafe, ySpeed, depthStrider, cancelSneak);
        this.offerListeners(
                new LambdaListener<>(MoveEvent.class, event -> {
                    switch (detection.getValue()) {
                        case STRICT: {
                            if (!mc.player.onGround) {
                                if (mc.player.isInsideOfMaterial(Material.LAVA)) {
                                    doSpeedLava(event);
                                } else if (mc.player.isInsideOfMaterial(Material.WATER)) {
                                    doSpeedWater(event);
                                }
                                break;
                            }
                        }
                        case FAST: {
                            if (!PositionUtil.inLiquid()) {
                                timer.reset();
                            }
                            if (timer.passed(250)) {
                                if (mc.player.isInWater()) {
                                    doSpeedWater(event);
                                }
                                if (mc.player.isInLava()) {
                                    doSpeedLava(event);
                                }
                            }
                        }
                        break;
                    }
                }),
                new LambdaListener<>(PacketEvent.Send.class, CPacketEntityAction.class, event -> {
                    CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
                    if (cancelSneak.getValue() && packet.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
                        switch (detection.getValue()) {
                            case STRICT: {
                                if (mc.player.isInsideOfMaterial(Material.LAVA) || mc.player.isInsideOfMaterial(Material.WATER)) {
                                    event.setCanceled(true);
                                }
                            }
                            case FAST: {
                                if (mc.player.isInLava() || mc.player.isInWater()) {
                                    event.setCanceled(true);
                                }
                            }
                        }
                    }
                }));
    }

    private void doSpeedWater(MoveEvent event) {
        ItemStack stack = mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!depthStrider.getValue() && hasDepthStrider(stack)) {
            return;
        }
        if (strafe.getValue()) {
            double[] strafe = MovementUtil.strafe(waterSpeed.getValue() / 10);
            event.setX(strafe[0]);
            doVerticalSpeed(event);
            event.setZ(strafe[1]);
        }
        else {
            event.setX(event.getX() * waterSpeed.getValue());
            doVerticalSpeed(event);
            event.setZ(event.getZ() * waterSpeed.getValue());
        }
    }

    private void doSpeedLava(MoveEvent event) {
        if (mc.player.isElytraFlying() && elytra.getValue()) {
            event.setX(event.getX() * elytraSpeed.getValue());
            doVerticalSpeed(event);
            event.setZ(event.getZ() * elytraSpeed.getValue());
        }
        if (strafe.getValue()) {
            double[] strafe = MovementUtil.strafe(lavaSpeed.getValue() / 10);
            event.setX(strafe[0]);
            doVerticalSpeed(event);
            event.setZ(strafe[1]);
        } else {
            event.setX(event.getX() * lavaSpeed.getValue());
            doVerticalSpeed(event);
            event.setZ(event.getZ() * lavaSpeed.getValue());
        }
    }

    private void doVerticalSpeed(MoveEvent event) {
        if (mc.gameSettings.keyBindJump.isKeyDown() && ySpeed.getValue()) {
            event.setY(event.getY() + 0.16);
        } else if (mc.gameSettings.keyBindSneak.isKeyDown() && ySpeed.getValue()) {
            event.setY(event.getY() - 0.12);
        }
    }

    private boolean hasDepthStrider(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) > 0;
    }
}
