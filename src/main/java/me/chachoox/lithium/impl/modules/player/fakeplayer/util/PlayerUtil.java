package me.chachoox.lithium.impl.modules.player.fakeplayer.util;

import com.mojang.authlib.GameProfile;
import me.chachoox.lithium.api.interfaces.Minecraftable;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

//add this to the module
public class PlayerUtil implements Minecraftable {
    private static EntityOtherPlayerMP fake;

    public static void addFakePlayerToWorld(String name, int id) {
        if (mc.player != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), name); //"\u6495\u6BC1\u5E03\u9C81\u8D6B"
            fake = new EntityOtherPlayerMP(mc.world, profile);
            fake.inventory.copyInventory(mc.player.inventory);
            fake.copyLocationAndAnglesFrom(mc.player);
            fake.setHealth(mc.player.getHealth());
            fake.setAbsorptionAmount(mc.player.getAbsorptionAmount());
            fake.onGround = mc.player.onGround;
            mc.world.addEntityToWorld(id, fake);
        }
    }

    public static void removeFakePlayerFromWorld(int id) {
        if (mc.player != null) {
            mc.world.removeEntityFromWorld(id);
            fake = null;
        }
    }

    public static EntityOtherPlayerMP getPlayer() {
        return fake;
    }
}
