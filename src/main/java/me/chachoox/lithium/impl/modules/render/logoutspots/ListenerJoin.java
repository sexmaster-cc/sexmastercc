package me.chachoox.lithium.impl.modules.render.logoutspots;

import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.TextColor;
import me.chachoox.lithium.impl.event.events.network.ConnectionEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.render.logoutspots.util.LogoutSpot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ListenerJoin extends ModuleListener<LogoutSpots, ConnectionEvent.Join> {
    public ListenerJoin(LogoutSpots module) {
        super(module, ConnectionEvent.Join.class);
    }

    @Override
    public void call(ConnectionEvent.Join event) {
        if (event.getName().equals(mc.getSession().getProfile().getName())) {
            return;
        }

        LogoutSpot spot = module.spots.remove(event.getUuid());

        if (module.message.getValue()) {
            String text;
            if (spot != null) {
                Vec3d pos = spot.rounded();
                text = TextColor.RED + event.getName() + " is back at: "
                        + pos.x + ", "
                        + pos.y + ", "
                        + pos.z + ".";
            } else {
                EntityPlayer player = event.getPlayer();
                if (player != null) {
                    text = TextColor.GREEN + player.getName() + " joined at: %s, %s, %s.";
                    text = String.format(text, (int) player.posX, (int) player.posY, (int) player.posZ);
                } else {
                    return;
                }
            }

            Logger.getLogger().log(text);
        }
    }
}
