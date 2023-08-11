package me.chachoox.lithium.impl.modules.movement.tunnelspeed;

import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ListenerTick extends ModuleListener<TunnelSpeed, TickEvent> {
    public ListenerTick(TunnelSpeed module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (mc.player != null) {
            BlockPos above = new BlockPos(mc.player.posX, mc.player.posY + 2.0f, mc.player.posZ);
            BlockPos below = new BlockPos(mc.player.posX, mc.player.posY - 1.0f, mc.player.posZ);
            if ((mc.world.getBlockState(above).getBlock() != Blocks.AIR)
                    && (mc.world.getBlockState(above).getBlock() != Blocks.LAVA)
                    && (mc.world.getBlockState(above).getBlock() != Blocks.FLOWING_LAVA)
                    && (mc.world.getBlockState(above).getBlock() != Blocks.WATER)
                    && (mc.world.getBlockState(above).getBlock() != Blocks.FLOWING_WATER)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.PACKED_ICE)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.ICE)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.FROSTED_ICE)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.WATER)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.FLOWING_WATER)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.LAVA)
                    && (mc.world.getBlockState(below).getBlock() != Blocks.FLOWING_LAVA)
                    && mc.gameSettings.keyBindForward.isKeyDown()
                    && (!mc.gameSettings.keyBindSneak.isKeyDown())
                    && !Managers.MODULE.get(PacketFly.class).isEnabled()
                    && !Managers.MODULE.get(NoClip.class).isEnabled()
                    && (mc.player.onGround))
            {
                module.tunnel = true;
                mc.player.motionX -= Math.sin(Math.toRadians(mc.player.rotationYaw)) * 0.15f;
                mc.player.motionZ += Math.cos(Math.toRadians(mc.player.rotationYaw)) * 0.15f;
            } else {
                module.tunnel = false;
            }
        }
    }
}
