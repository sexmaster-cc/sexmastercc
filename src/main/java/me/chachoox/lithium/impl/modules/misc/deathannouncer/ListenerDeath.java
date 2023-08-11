package me.chachoox.lithium.impl.modules.misc.deathannouncer;

import me.chachoox.lithium.impl.event.events.entity.DeathEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ListenerDeath extends ModuleListener<DeathAnnouncer, DeathEvent> {
    public ListenerDeath(DeathAnnouncer module) {
        super(module, DeathEvent.class);
    }

    @Override
    public void call(DeathEvent event) {
        if (mc.player == null) {
            return;
        }

        if (event.getEntity() instanceof EntityPlayer) {
            EntityLivingBase player = event.getEntity();
            if (player == mc.player || Managers.FRIEND.isFriend((EntityPlayer) player)) {
                return;
            }

            if (mc.player.getPosition().getY() > module.yLevel.getValue()) {
                return;
            }

            if (!module.timer.passed(module.delay.getValue() * 1000)) {
                return;
            }

            module.name = player.getName();

            if (mc.player.getDistance(player) < module.range.getValue()) {
                switch (module.killSayPreset.getValue()) {
                    case TROLLGOD: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.trollGodMessage());
                        break;
                    }
                    case POLLOSMOD: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.pollosMessage());
                        break;
                    }
                    case TROLLHACK: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.trollHackMessages());
                        break;
                    }
                    case ABYSS: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.abyssMessage());
                        break;
                    }
                    case PHOBOS: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.phobosMessage());
                        break;
                    }
                    case KONAS: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.konasMessages());
                        break;
                    }
                    case WURSTPLUS: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.wurstPlusMessages());
                        break;
                    }
                    case AURORA: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.auroraMessage());
                        break;
                    }
                    case AUTISM: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.autismMessage());
                        break;
                    }
                    case KAMI: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.kamiMessage());
                        break;
                    }
                    case WELLPLAYED: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.wellPlayedMessages());
                        break;
                    }
                    case PRAYER: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.prayerMessage());
                        break;
                    }
                    case POLLOS: { //he actually asked me for this
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                "GG!");
                        break;
                    }
                    case NFT: {
                        mc.player.sendChatMessage((
                                module.greenText.getValue() ? "> " : "") +
                                module.nftMessage());
                        break;
                    }
                }
                module.timer.reset();
            }
        }
    }
}

