package me.chachoox.lithium.impl.modules.movement.noaccel;

import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.movement.fly.Fly;
import me.chachoox.lithium.impl.modules.movement.holepull.HolePull;
import me.chachoox.lithium.impl.modules.movement.noclip.NoClip;
import me.chachoox.lithium.impl.modules.movement.packetfly.PacketFly;
import me.chachoox.lithium.impl.modules.movement.speed.Speed;
import me.chachoox.lithium.impl.modules.movement.tunnelspeed.TunnelSpeed;

public class ListenerMove extends ModuleListener<NoAccel, MoveEvent> {
    public ListenerMove(NoAccel module) {
        super(module, MoveEvent.class);
    }

    @Override
    public void call(MoveEvent event) {
        if (mc.player.isElytraFlying()
                || mc.player.capabilities.isFlying
                || mc.player.noClip
                || Managers.MODULE.get(NoClip.class).isEnabled()
                || Managers.MODULE.get(HolePull.class).isEnabled() && Managers.MODULE.get(HolePull.class).isAnchoring()
                || Managers.MODULE.get(PacketFly.class).isEnabled()
                || Managers.MODULE.get(Speed.class).isEnabled()
                || Managers.MODULE.get(Fly.class).isEnabled()
                || Managers.MODULE.get(TunnelSpeed.class).isEnabled() && Managers.MODULE.get(TunnelSpeed.class).isTunnel()
                || !Managers.STOP.passedLag(200) && module.stopOnLagback.getValue()
                || !Managers.STOP.passedLiquid() && !module.inWater.getValue()
                || !Managers.STOP.passedPop() && !module.stopOnTotem.getValue()
                || module.isEating() && !module.whileEating.getValue()
                || mc.player.isSpectator()
                || mc.player.getFoodStats().getFoodLevel() <= 6f) {
            return;
        }

        if (!module.sneak.getValue() || (!mc.player.isSneaking())) {
            module.strafe(event, module.getSpeed(module.slow.getValue(),
                    module.speed.getValue()) * (Managers.KNOCKBACK.shouldBoost(module.kbBoost.getValue()) ? module.boostReduction.getValue() : 1.0));
        }
    }
}
