package me.chachoox.lithium.impl.modules.player.fakeplayer;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.impl.modules.player.fakeplayer.util.PlayerUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.player.fakeplayer.position.Position;

public class ListenerMotion extends ModuleListener<FakePlayer, MotionUpdateEvent> {
    public ListenerMotion(FakePlayer module) {
        super(module, MotionUpdateEvent.class);
    }

    private boolean wasRecording;
    private int ticks;
    private int i;

    @Override
    public void call(MotionUpdateEvent event) {
        if (PlayerUtil.getPlayer() != null) {
            if (event.getStage() == Stage.PRE && !module.record.getValue()) {
                if (module.playRecording.getValue()) {
                    if (module.playerPositions.isEmpty()) {
                        module.playRecording.setValue(false);
                        return;
                    }

                    if (i >= module.playerPositions.size()) {
                        i = 0;
                    }

                    if (ticks++ % 2 == 0) {
                        Position p = module.playerPositions.get(i++);
                        PlayerUtil.getPlayer().rotationYaw = p.getYaw();
                        PlayerUtil.getPlayer().rotationPitch = p.getPitch();
                        PlayerUtil.getPlayer().rotationYawHead = p.getHead();
                        PlayerUtil.getPlayer().setPositionAndRotationDirect(p.getX(), p.getY(), p.getZ(), p.getYaw(), p.getPitch(), 3, false);
                        PlayerUtil.getPlayer().motionX = p.getMotionX();
                        PlayerUtil.getPlayer().motionY = p.getMotionY();
                        PlayerUtil.getPlayer().motionZ = p.getMotionZ();
                    }
                } else {
                    i = 0;
                    PlayerUtil.getPlayer().motionX = 0.0;
                    PlayerUtil.getPlayer().motionY = 0.0;
                    PlayerUtil.getPlayer().motionZ = 0.0;
                }
            } else if (event.getStage() == Stage.POST && module.record.getValue()) {
                module.playRecording.setValue(false);
                PlayerUtil.getPlayer().motionX = 0.0;
                PlayerUtil.getPlayer().motionY = 0.0;
                PlayerUtil.getPlayer().motionZ = 0.0;

                if (!wasRecording) {
                    module.playerPositions.clear();
                    wasRecording = true;
                }

                if (ticks++ % 2 == 0) {
                    module.playerPositions.add(new Position(mc.player));
                }
            }
        }
    }
}
