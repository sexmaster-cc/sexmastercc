package me.chachoox.lithium.impl.modules.movement.holepull;

import me.chachoox.lithium.api.util.blocks.HoleUtil;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.movement.PositionUtil;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.movement.actions.MoveEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.movement.holepull.mode.PullMode;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ListenerMove extends ModuleListener<HolePull, MoveEvent> {
    public ListenerMove(HolePull module) {
        super(module, MoveEvent.class, 1000);
    }

    @Override
    public void call(MoveEvent event) {
        if (mc.player.isSpectator()) {
            return;
        }
        if (module.mode.getValue() == PullMode.PULL) {
            if (module.isPitchDown() && !mc.player.isSneaking()) {
                BlockPos pos = PositionUtil.getPosition();
                if (HoleUtil.isHole(pos.down(1))
                        || HoleUtil.isHole(pos.down(2))
                        || HoleUtil.isHole(pos.down(3))
                        || HoleUtil.isHole(pos.down(4))
                        || HoleUtil.isHole(pos.down(5))) {
                    module.anchoring = true;
                    module.doPull(event, PositionUtil.getPosition());
                } else {
                    module.anchoring = false;
                }

            } else {
                module.anchoring = false;
            }
        }

        if (module.mode.getValue() == PullMode.SNAP) {
            if (EntityUtil.isPlayerSafe(mc.player) || module.isSafe(module.twoVec)) {
                Logger.getLogger().log(TextColor.RED + "<HolePull> Entered a hole.", 45088);
                module.disable();
            }
            if (module.hole != null && mc.world.getBlockState(module.hole).getBlock() == Blocks.AIR) {
                module.doPull(event, module.hole);
                if ((mc.player.collidedHorizontally && mc.player.onGround)) {
                    ++module.stuck;
                    if (module.stuck == 10) {
                        Logger.getLogger().log(TextColor.RED + "<HolePull> Player got stuck.", 45088);
                        module.disable();
                    }
                }
                else {
                    module.stuck = 0;
                }
            }
            else {
                Logger.getLogger().log(TextColor.RED + "<HolePull> Hole no longer exists.", 45088);
                module.disable();
            }
        }
    }
}
