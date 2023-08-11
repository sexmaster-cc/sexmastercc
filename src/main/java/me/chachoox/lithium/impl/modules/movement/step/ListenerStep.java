package me.chachoox.lithium.impl.modules.movement.step;

import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.StepEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.step.mode.StepMode;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerStep extends ModuleListener<Step, StepEvent> {

    public ListenerStep(Step module) {
        super(module, StepEvent.class);
    }

    @Override
    public void call(StepEvent event) {
        if (module.mode.getValue() == StepMode.NORMAL) {
            double stepHeight = event.getBB().minY - mc.player.posY;

            if (stepHeight <= 0 || stepHeight > (double) module.height.getValue()) {
                return;
            }
            double[] offsets = module.getOffset(stepHeight);

            if (offsets != null && offsets.length > 1) {
                for (double offset : offsets) {
                    PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset, mc.player.posZ, false));
                }
            }

            module.timer.reset();
        }
    }
}