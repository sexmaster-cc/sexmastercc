package me.chachoox.lithium.impl.modules.render.animations;

import me.chachoox.lithium.asm.mixins.network.client.ICPacketAnimation;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class ListenerSwing extends ModuleListener<Animations, PacketEvent.Send<CPacketAnimation>> {
    public ListenerSwing(Animations module) {
        super(module, PacketEvent.Send.class, CPacketAnimation.class);
    }

    @Override
    public void call(PacketEvent.Send<CPacketAnimation> event) {
        if (module.swing.getValue() == SwingEnum.NONE) {
            return;
        }

        if (module.swing.getValue() == SwingEnum.CANCEL) {
            event.setCanceled(true);
            return;
        }

        final CPacketAnimation packet = event.getPacket();
        EnumHand hand = null;
        switch (module.swing.getValue()) {
            case OFFHAND: {
                hand = EnumHand.OFF_HAND;
                break;
            }
            case MAINHAND: {
                hand = EnumHand.MAIN_HAND;
                break;
            }
        }

        ((ICPacketAnimation) packet).setHand(hand);
    }
}
