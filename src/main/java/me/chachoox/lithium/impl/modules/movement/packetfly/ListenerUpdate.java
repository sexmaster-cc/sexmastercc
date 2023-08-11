package me.chachoox.lithium.impl.modules.movement.packetfly;

import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.LimitMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PacketFlyMode;
import me.chachoox.lithium.impl.modules.movement.packetfly.mode.PhaseMode;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ListenerUpdate extends ModuleListener<PacketFly, UpdateEvent> {
    public ListenerUpdate(PacketFly module) {
        super(module, UpdateEvent.class);
    }

    @Override
    public void call(UpdateEvent event) {
        module.teleportMap.entrySet().removeIf(module::clearMap);
        mc.player.motionZ = 0.0;
        mc.player.motionY = 0.0;
        mc.player.motionX = 0.0;
        if (module.mode.getValue() != PacketFlyMode.SETBACK && module.lastTpID == 0) {
            if (module.resetCounter(4)) {
                module.sendPackets(0.0, 0.0, 0.0, true);
            }
            return;
        }

        boolean hitBox = module.checkHitBox();

        double rawSpeed = mc.player.movementInput.jump
                && (hitBox || !MovementUtil.isMoving() || lagSpeed()) ? (module.antiKick.getValue()
                && !hitBox ? (module.resetCounter(module.mode.getValue() == PacketFlyMode.SETBACK ? 10 : 20) ? -0.032 : 0.062) : 0.062)
                : (mc.player.movementInput.sneak ? -0.062 : (!hitBox ? (module.resetCounter(4) ? (module.antiKick.getValue() ? -0.04 : 0.0) : 0.0) : 0.0));

        double conSpeed = mc.player.movementInput.jump
                && (hitBox || !MovementUtil.isMoving() || lagSpeed()) ? (module.antiKick.getValue()
                && !hitBox ? (module.resetCounter(module.mode.getValue() == PacketFlyMode.SETBACK ? 10 : 20) ? -module.concealOffset / 2 : module.concealOffset) : module.concealOffset)
                : (mc.player.movementInput.sneak ? -module.concealOffset : (!hitBox ? (module.resetCounter(4) ? (module.antiKick.getValue() ? -0.04 : 0.0) : 0.0) : 0.0));

        double speed = module.conceal.getValue() ? conSpeed : rawSpeed;

        if (module.phase.getValue() == PhaseMode.FULL && hitBox && MovementUtil.isMoving() && speed != 0.0) {
            speed = speed / 2.5;
        }

        if (mc.player.movementInput.jump && module.autoClip.getValue()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, module.isSneaking ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
            module.isSneaking = !module.isSneaking;
        }

        double[] dirSpeed = MovementUtil.strafe(module.phase.getValue().equals(PhaseMode.FULL) && hitBox ? 0.031 : 0.26);
        float rawFactor = module.factor.getValue();
        int factorInt = (int) Math.floor(rawFactor);

        if (module.mode.getValue() == PacketFlyMode.FACTOR && ticksExisted()) {
            float extraFactor = rawFactor - (float) factorInt;
            if (Math.random() <= extraFactor) {
                factorInt++;
            }
        }

        for (int i = 1; i <= factorInt; ++i) {
            if (module.mode.getValue() == PacketFlyMode.LIMIT) {
                if (mc.player.ticksExisted % 2 == 0) {
                    if (module.limited && speed >= 0.0) {
                        module.limited = false;
                        speed = -0.032;
                    }
                    mc.player.motionX = dirSpeed[0] * i;
                    mc.player.motionY = speed * i;
                    mc.player.motionZ = dirSpeed[1] * i;
                    module.sendPackets(mc.player.motionX, mc.player.motionY, mc.player.motionZ, false);
                    continue;
                }
                if (!(speed < 0.0)) {
                    continue;
                }
                module.limited = true;
                continue;
            }
            mc.player.motionX = dirSpeed[0] * i;
            mc.player.motionY = speed * i;
            mc.player.motionZ = dirSpeed[1] * i;
            module.sendPackets(mc.player.motionX, mc.player.motionY, mc.player.motionZ, module.mode.getValue() != PacketFlyMode.SETBACK);
        }
    }

    private boolean ticksExisted() {
        if (module.limit.getValue() == LimitMode.NONE) {
            return true;
        }
        if (module.limit.getValue() == LimitMode.BOTH || module.limit.getValue() == LimitMode.TICK) {
            return mc.player.ticksExisted % 15 > 0;
        } else {
            return true;
        }
    }

    private boolean lagSpeed() {
        if (module.limit.getValue() == LimitMode.NONE) {
            return true;
        }
        if (module.limit.getValue() == LimitMode.BOTH || module.limit.getValue() == LimitMode.SPEED) {
            return --module.lagTime > 0;
        } else {
            return true;
        }
    }
}