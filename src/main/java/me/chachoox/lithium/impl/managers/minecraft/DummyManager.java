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
import me.chachoox.lithium.impl.modules.other.chat.ChatBridge;
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
                    runTutorial();
                    sentTut = true;
                    Managers.MODULE.get(ChatBridge.class).setEnabled(true);
                    Managers.MODULE.get(DisplayTweaks.class).setEnabled(true);
                }
            }
        });
    }

    public static void runTutorial() {
        Logger.getLogger().logNoMark("1. Default prefix is comma", 0x21);
        Logger.getLogger().logNoMark("2. To bind the clickgui use command -> bind clickgui <key>", 0x22);
        Logger.getLogger().logNoMark("3. Use command -> cmds for a list of commands", 0x23);
        Logger.getLogger().logNoMark("4. Colour and string properties are hidden use command -> modulename list | for a list of the properties", 0x24);
        Logger.getLogger().logNoMark("5. To handle colour property values use these commands", 0x25);
        Logger.getLogger().logNoMark("6. modulename colourpropertyname <r/g/b/a> <value>", 0x26);
        Logger.getLogger().logNoMark("7. or to set RGB values all in one use command", 0x27);
        Logger.getLogger().logNoMark("8. modulename colourpropertyname set <value> <value> <value>", 0x28);
        Logger.getLogger().logNoMark("9. You can also copy/paste colour property values using these commands", 0x29);
        Logger.getLogger().logNoMark("10. modulename colourpropertyname copy", 0x30);
        Logger.getLogger().logNoMark("11. modulename colourpropertyname paste", 0x31);
        Logger.getLogger().logNoMark("12. To handle string properties use command", 0x32);
        Logger.getLogger().logNoMark("13. modulename stringpropertyname <value>", 0x33);
        Logger.getLogger().logNoMark("14. Begin a chat message with % to send it into the Chat Bridge", 0x34);
        Logger.getLogger().logNoMark("15. Use command -> drawn modulename | to hide it on the arraylist", 0x35);
        Logger.getLogger().logNoMark("16. To change the prefix use command -> prefix <key>", 0x36);
    }

    public boolean isValid() {
        return isValidScreen;
    }
}