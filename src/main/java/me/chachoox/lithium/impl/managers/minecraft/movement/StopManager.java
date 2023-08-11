package me.chachoox.lithium.impl.managers.minecraft.movement;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.impl.event.events.entity.TotemPopEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;

public class StopManager extends SubscriberImpl implements Minecraftable {

    private final StopWatch liquidTimer = new StopWatch();
    private final StopWatch lagTimer = new StopWatch();
    private final StopWatch popTimer = new StopWatch();

    public StopManager() {
        this.listeners.add(new Listener<TickEvent>(TickEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(TickEvent event) {
                if (event.isSafe()) {
                    BlockPos pos = PositionUtil.getPosition();
                    IBlockState state = mc.world.getBlockState(pos);
                    IBlockState stateUp = mc.world.getBlockState(pos.up());
                    if (state.getBlock() instanceof BlockLiquid || stateUp.getBlock() instanceof BlockLiquid) {
                        liquidTimer.reset();
                    }
                }
            }
        });
        this.listeners.add(new Listener<PacketEvent.Receive<SPacketPlayerPosLook>>
                (PacketEvent.Receive.class, Integer.MIN_VALUE, SPacketPlayerPosLook.class) {
            @Override
            public void call(PacketEvent.Receive<SPacketPlayerPosLook> event) {
                if (mc.player != null) {
                    lagTimer.reset();
                }
            }
        });
        this.listeners.add(new Listener<TotemPopEvent>(TotemPopEvent.class) {
            @Override
            public void call(TotemPopEvent event) {
                if (event.getEntity() == mc.player) {
                    popTimer.reset();
                }
            }
        });
    }

    public boolean passedLiquid() {
        return liquidTimer.passed(500L);
    }

    public boolean passedLag(int delay) {
        return lagTimer.passed(delay);
    }

    public boolean passedPop() {
        return popTimer.passed(500);
    }
}
