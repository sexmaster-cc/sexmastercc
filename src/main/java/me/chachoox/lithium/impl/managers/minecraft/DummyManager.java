package me.chachoox.lithium.impl.managers.minecraft;

import me.chachoox.lithium.Lithium;
import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.SubscriberImpl;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.asm.mixins.network.client.ICPacketPlayerTryUseItemOnBlock;
import me.chachoox.lithium.impl.event.events.network.DisconnectEvent;
import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.events.update.UpdateEvent;
import me.chachoox.lithium.impl.event.events.world.WorldClientEvent;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.render.displaytweaks.DisplayTweaks;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

//ima use this for random stuff that doesnt make sense as modules/managers
public class DummyManager extends SubscriberImpl implements Minecraftable {
    private boolean isValidScreen;
    private boolean sentTut;

    public DummyManager() {
        this.listeners.add(new Listener<DisconnectEvent>(DisconnectEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(DisconnectEvent event) { // self logout
                if (mc.player != null && mc.getCurrentServerData() != null) {
                    BlockPos pos = mc.player.getPosition();
                    Logger.getLogger().log(Level.INFO, "SelfLogout: "
                            + "(Server: "
                            + mc.getCurrentServerData().serverIP
                            + ")"
                            + " (XYZ: "
                            + pos.getX()
                            + ", "
                            + pos.getY()
                            + ", "
                            + pos.getZ() + ")"
                    );
                }
            }
        });
        this.listeners.add(new Listener<UpdateEvent>(UpdateEvent.class, Integer.MIN_VALUE) {
            @Override
            public void call(UpdateEvent event) {
                isValidScreen = mc.currentScreen instanceof GuiScreenHorseInventory;
            }
        });
        this.listeners.add(new Listener<PacketEvent.Send<CPacketPlayerTryUseItemOnBlock>>(PacketEvent.Send.class, CPacketPlayerTryUseItemOnBlock.class) { // build height
            @Override
            public void call(PacketEvent.Send<CPacketPlayerTryUseItemOnBlock> event) {
                CPacketPlayerTryUseItemOnBlock packet = event.getPacket();
                if (packet.getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
                    ((ICPacketPlayerTryUseItemOnBlock) packet).setFacing(EnumFacing.DOWN);
                }
            }
        });
        this.listeners.add(new Listener<WorldClientEvent.Load>(WorldClientEvent.Load.class) { // tutorial
            @Override
            public void call(WorldClientEvent.Load event) {// shout out to cpv for finding thesec razy bugs
                if (Lithium.firstTimeLoaded && !sentTut) {
                    Logger.getLogger().log("Prefix is [,] string properties & color properties have to be handled with commands.");
                    sentTut = true;
                    Managers.MODULE.get(DisplayTweaks.class).setEnabled(true);
                }
            }
        });
    }

    public boolean isValid() {
        return isValidScreen;
    }
}