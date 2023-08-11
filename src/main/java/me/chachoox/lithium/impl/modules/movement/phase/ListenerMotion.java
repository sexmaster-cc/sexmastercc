package me.chachoox.lithium.impl.modules.movement.phase;

import me.chachoox.lithium.api.event.events.Stage;
import me.chachoox.lithium.api.util.movement.MovementUtil;
import me.chachoox.lithium.api.util.network.PacketUtil;
import me.chachoox.lithium.impl.event.events.movement.MotionUpdateEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.network.play.client.CPacketPlayer;

public class ListenerMotion extends ModuleListener<Phase, MotionUpdateEvent> {
    public ListenerMotion(Phase module) {
        super(module, MotionUpdateEvent.class);
    }

    @Override
    public void call(MotionUpdateEvent event) {
        if (!module.timer.passed(module.delay.getValue() * 1000L)) {
            return;
        }

        switch (module.mode.getValue()) {
            case PACKET: {
                if (event.getStage() == Stage.POST) {
                    double xOff1;
                    double zOff1;

                    double multiplier1 = 0.3D;

                    double mx1 = Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0F));
                    double mz1 = Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0F));

                    xOff1 = mc.player.movementInput.moveForward * multiplier1 * mx1 + mc.player.movementInput.moveStrafe * multiplier1 * mz1;
                    zOff1 = mc.player.movementInput.moveForward * multiplier1 * mz1 - mc.player.movementInput.moveStrafe * multiplier1 * mx1;

                    PacketUtil.send(new CPacketPlayer.Position(mc.player.posX + xOff1, mc.player.posY, mc.player.posZ + zOff1, false));

                    for (int i = 1; i < 10; i++) {
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, 8.988465674311579E307D, mc.player.posZ, false));
                    }

                    mc.player.setPosition(mc.player.posX + xOff1, mc.player.posY, mc.player.posZ + zOff1);
                }
                break;
            }
            case ZOOM: {
                mc.player.motionY = 0.0D;

                double xOff;
                double zOff;
                double mx = Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0F));
                double mz = Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0F));

                xOff = mc.player.movementInput.moveForward * 0.152D * mx + mc.player.movementInput.moveStrafe * 0.152D * mz;
                zOff = mc.player.movementInput.moveForward * 0.152D * mz - mc.player.movementInput.moveStrafe * 0.152D * mx;

                PacketUtil.send(new CPacketPlayer.Position(
                        mc.player.posX + mc.player.motionX * 11.0D + xOff,
                        mc.player.posY + (mc.gameSettings.keyBindJump.isPressed() ? 0.0624D : module.zoomies ? 0.0625D : 1.0E-8D) - (mc.gameSettings.keyBindSneak.isPressed() ? 0.0624D : module.zoomies ? 0.0625D : 2.0E-8D),
                        mc.player.posZ + mc.player.motionZ * 11.0D + zOff,
                        false)
                );

                PacketUtil.send(new CPacketPlayer.Position(
                        mc.player.posX + mc.player.motionX * 11.0D + xOff,
                        1337.0D + mc.player.posY,
                        mc.player.posZ + mc.player.motionZ * 11.0D + zOff,
                        false)
                );

                mc.player.setPositionAndUpdate(mc.player.posX + xOff, mc.player.posY, mc.player.posZ + zOff);
                module.zoomies = (!module.zoomies);
                break;
            }
            case INFINITE: {
                mc.player.motionY = 0D;

                switch (Hud.getDirection4D()) {
                    case 0:
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ + 0.5, mc.player.onGround));
                        mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ + 1);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        break;
                    case 1:
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX - 0.5, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.setPosition(mc.player.posX - 1, mc.player.posY, mc.player.posZ);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        break;
                    case 2:
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ - 0.5, mc.player.onGround));
                        mc.player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ - 1);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        break;
                    case 3:
                    default:
                        PacketUtil.send(new CPacketPlayer.Position(mc.player.posX + 0.5, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        mc.player.setPosition(mc.player.posX + 1, mc.player.posY, mc.player.posZ);
                        PacketUtil.send(new CPacketPlayer.Position(Double.POSITIVE_INFINITY, mc.player.posY, mc.player.posZ, mc.player.onGround));
                        break;
                }
                break;
            }
            case SKIP: { //TOO MANY OFFSETS
                double[] yOffsets = new double[] {0.02500000037252903, 0.028571428997176036, 0.033333333830038704, 0.04000000059604645, 0.05000000074505806, 0.06666666766007741, 0.10000000149011612, 0.20000000298023224, 0.04000000059604645, 0.033333333830038704, 0.028571428997176036, 0.02500000037252903};
                double[] dirSpeed = MovementUtil.strafe(0.031);

                for (int index = 0; index < yOffsets.length; index++) {
                    PacketUtil.send(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + yOffsets[index], mc.player.posZ, mc.player.onGround));
                    PacketUtil.send(new CPacketPlayer.Position(mc.player.posX + (dirSpeed[0] * index), mc.player.posY, mc.player.posZ + (dirSpeed[1] * index), mc.player.onGround));
                }
                break;
            }
        }

        module.timer.reset();
    }
}
