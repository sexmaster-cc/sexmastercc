package me.chachoox.lithium.impl.modules.player.quiver;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.rotation.RotationUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ListenerMotion extends ModuleListener<Quiver, MotionUpdateEvent>
{
    private PotionType lastType;
    private long lastDown;

    public ListenerMotion(Quiver module)
    {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        ItemStack arrow;
        EnumHand hand = ItemUtil.getHand(Items.BOW);
        if (mc.player.isCreative() || mc.currentScreen != null || hand == null || (arrow = module.findArrow()).isEmpty() || blocked()) {
            return;
        }

        boolean cycle = true;
        if (module.badStack(arrow) || module.fast) {
            if (!cycle) {
                return;
            }

            module.cycle(false, true);
            module.fast = false;
            arrow = module.findArrow();
            if (module.badStack(arrow)) {
                return;
            }
        }

        if (event.getStage() == Stage.PRE) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                lastDown = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - lastDown > 100) {
                return;
            }

            EntityPlayer player = mc.player;
            if (player.motionX != 0 || player.motionZ != 0) {
                Vec3d vec3d = player.getPositionVector().add(player.motionX, player.motionY + player.getEyeHeight(), player.motionZ);
                float[] rotations = RotationUtil.getRotations(vec3d);
                mc.player.rotationYaw = rotations[0];
                mc.player.rotationPitch = rotations[1];
            } else {
                mc.player.rotationPitch = (-90.0f);
            }
        } else if (!mc.player.getActiveItemStack().isEmpty()) {
            PotionType type = PotionUtils.getPotionFromItem(arrow);
            if (arrow.getItem() instanceof ItemSpectralArrow) {
                type = Quiver.SPECTRAL;
            }

            if (lastType == type && !module.timer.passed(module.shootDelay.getValue())) {
                return;
            }

            lastType = type;
            float ticks = mc.player.getHeldItem(hand).getMaxItemUseDuration() - mc.player.getItemInUseCount() - 0.0f;
            if (ticks >= module.releaseTicks.getValue() && ticks <= module.maxTicks.getValue()) {
                mc.playerController.onStoppedUsingItem(mc.player);
                module.fast = cycle;
                module.timer.reset();
            }
        }
    }

    private boolean blocked() {
        BlockPos pos = PositionUtil.getPosition();
        return mc.world.getBlockState(pos.up())
                .getMaterial()
                .blocksMovement()
                || mc.world.getBlockState(pos.up(2))
                .getMaterial()
                .blocksMovement();
    }

}