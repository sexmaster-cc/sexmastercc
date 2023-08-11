package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import me.chachoox.lithium.impl.event.events.network.PacketEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class ListenerSound extends ModuleListener<PvPInfo, PacketEvent.Receive<SPacketSoundEffect>> {
    public ListenerSound(PvPInfo module) {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void call(PacketEvent.Receive<SPacketSoundEffect> event) {
        final SPacketSoundEffect packet = event.getPacket();
        /*
        if (!module.announcePotions()) {
            return;
        }

        if (packet.getSound().getSoundName().toString().equalsIgnoreCase("minecraft:entity.generic.drink")) {
            List<EntityPlayer> players = getPlayersViaSoundPacket(packet);
            EntityPlayer drinker = null;
            if (players.size() > 1) {
                for (EntityPlayer player : players) {
                    if (drinker == null || player.getDistance(packet.getX(), packet.getY(), packet.getZ()) < drinker.getDistance(packet.getX(), packet.getY(), packet.getZ())) {
                        drinker = player;
                    }
                }
            } else {
                drinker = players.get(0);
            }

            if (drinker == null) {
                return;
            }

            Item potion = drinker.getHeldItemMainhand().getItem();
            if (potion instanceof ItemPotion && module.drinkers.get(drinker) == null) {
                module.drinkers.put(drinker, (ItemPotion) potion);
            }

        } else if (packet.getSound().getSoundName().toString().equalsIgnoreCase("minecraft:item.armor.equip_generic")) {
            List<EntityPlayer> players = getPlayersViaSoundPacket(packet);
            EntityPlayer drinker = null;
            if (players.size() > 1) {
                for (EntityPlayer player : players) {
                    if (drinker == null || player.getDistance(packet.getX(), packet.getY(), packet.getZ()) < drinker.getDistance(packet.getX(), packet.getY(), packet.getZ())) {
                        drinker = player;
                    }
                }
            } else {
                drinker = players.get(0);
            }

            if (drinker == null) {
                return;
            }

            ItemPotion potion = module.drinkers.get(drinker);
            Item drinkerItem = drinker.getHeldItemMainhand().getItem();
            if (potion != null && drinkerItem instanceof ItemGlassBottle) {
                ResourceLocation resourceLocation = null;
                for (PotionEffect potionEffect : PotionUtils.getEffectsFromStack(drinkerItem.getDefaultInstance())) {
                    resourceLocation = potionEffect.getPotion().getRegistryName();
                }

                String name = drinker.getName();

                if (resourceLocation != null) {
                    Logger.getLogger().log(name + " has (drank) " + resourceLocation + "!", false);
                }

                module.drinkers.remove(drinker);
            }
        }
        */
    }

    public List<EntityPlayer> getPlayersViaSoundPacket(SPacketSoundEffect packet) {
        return mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(packet.getX() - 1, packet.getY() - 1, packet.getZ() - 1, packet.getX() + 1, packet.getY() + 1, packet.getZ() + 1));
    }
}
