package me.chachoox.lithium.impl.modules.misc.pvpinfo;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;
import me.chachoox.lithium.api.util.entity.EntityUtil;
import me.chachoox.lithium.api.util.inventory.ItemUtil;
import me.chachoox.lithium.api.util.math.StopWatch;
import me.chachoox.lithium.api.util.network.NetworkUtil;
import me.chachoox.lithium.impl.managers.Managers;
import me.chachoox.lithium.impl.modules.other.hud.Hud;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PvPInfo extends Module {

    private final NumberProperty<Integer> yPos =
            new NumberProperty<>(
                    0, -400, 40,
                    new String[]{"PosY", "y"},
                    "Y position of the hud."
            );

    private final NumberProperty<Integer> xPos =
            new NumberProperty<>(
                    2, 2, 720,
                    new String[]{"PosX", "x"},
                    "X position of the hud."
            );

    private final Property<Boolean> watermark =
            new Property<>(
                    true,
                    new String[]{"Watermark", "wmark", "mark"},
                    "Draws a watermark onto the hud."
            );

    private final Property<Boolean> offWaterMark =
            new Property<>(
                    true,
                    new String[]{"OffsetWatermark", "offwmark"},
                    "Separates the watermark from the rest of the hud."
            );

    private final StringProperty watermarkString =
            new StringProperty("sexmaster.cc",
                    new String[]{"WatermarkName", "markname"}
            );

    private final Property<Boolean> htr =
            new Property<>(
                    true,
                    new String[]{"Htr", "ht"},
                    "Draws hit range helper onto the hud."
            );

    private final Property<Boolean> plr =
            new Property<>(
                    true,
                    new String[]{"Plr", "pl"},
                    "Draws player range helper onto the hud."
            );

    private final Property<Boolean> totems =
            new Property<>(
                    true,
                    new String[]{"Totems", "tots"},
                    "Draws totem counter onto the hud."
            );

    private final Property<Boolean> ping =
            new Property<>(
                    true,
                    new String[]{"Ping", "latency"},
                    "Draws ping counter onto the hud."
            );

    private final Property<Boolean> lby =
            new Property<>(
                    true,
                    new String[]{"Lby", "lb"},
                    "Draws player range safety onto the hud."
            );

    /*
    private final Property<Boolean> potions =
            new Property<>(
                    false,
                    new String[]{"Strength", "str"},
                    "Sends a message when a player has strength. (BUGGY AS SHIT)"
            );
    */

    private final Property<Boolean> pearls =
            new Property<>(
                    true,
                    new String[]{"Pearls", "pearlsnotify"},
                    "Sends a message saying when a player threw a pearl."
            );


    private final Property<Boolean> pearlsCooldown =
            new Property<>(
                    false,
                    new String[]{"PearlCooldown", "pearlcool", "cooldown"},
                    "Renders pearl cooldown."
            );

    private final int GREEN = new Color(75, 255, 0).getRGB();
    private final int RED = Color.RED.getRGB();

    protected final ConcurrentHashMap<UUID, Integer> pearlsUUIDs = new ConcurrentHashMap<>();
    //protected final HashMap<EntityPlayer, ItemPotion> drinkers = new HashMap<>();

    protected final StopWatch pearlTimer = new StopWatch();
    protected boolean pearlThrown = false;

    public PvPInfo() {
        super("PvPInfo", new String[]{"PvPInfo", "pvpinformation", "dotgodmodule"}, "Information for pvp.", Category.MISC);
        this.offerProperties(yPos, xPos, watermark, offWaterMark, watermarkString, htr, plr, totems, ping, lby, pearls, pearlsCooldown);
        this.offerListeners(new ListenerRender(this), new ListenerUpdate(this));
    }

    protected void onRender(ScaledResolution resolution) {
        int width = resolution.getScaledWidth() / 2;
        int height = resolution.getScaledHeight() / 2;
        int offsetY = yPos.getValue() + height;
        int x = xPos.getValue();

        if (watermark.getValue()) {
            String watermarkStr = watermarkString.getValue();
            Managers.MODULE.get(Hud.class).renderText(watermarkStr, x, (!offWaterMark.getValue() ? offsetY : offsetY - 10));
            offsetY += 10;
        }

        if (htr.getValue()) {
            String htrStr = "HTR";
            Managers.FONT.drawString(htrStr, x, offsetY, getHTRColor());
            offsetY += 10;
        }

        if (plr.getValue()) {
            String plrStr = "PLR";
            Managers.FONT.drawString(plrStr, x, offsetY, getPLRColor());
            offsetY += 10;
        }

        if (totems.getValue()) {
            int totems = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
            String totemStr = String.valueOf(totems);
            Managers.FONT.drawString(totemStr, x, offsetY, getTotemColor(totems));
            offsetY += 10;
        }

        if (ping.getValue()) {
            String pingStr = "PING " + NetworkUtil.getLatency();
            Managers.FONT.drawString(pingStr, x, offsetY, getPingColor());
            offsetY += 10;
        }

        if (lby.getValue()) {
            String lbyStr = "LBY";
            Managers.FONT.drawString(lbyStr, x, offsetY, getLBYColor());
        }

        if (pearlsCooldown.getValue() && !pearlTimer.passed(15000)) {
            int pearlY = resolution.getScaledHeight() - 80;
            int pearlX = width - 189 + 9 * 20 + 2;
            String pearlString = String.format("%.2f", (pearlTimer.getTime() / 1000f)) + "s";
            Managers.MODULE.get(Hud.class).renderText(pearlString, pearlX, pearlY + 9);
        }
    }

    private int getHTRColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && mc.player.getDistance(entity) < 8.5F) {
            return GREEN;
        } else {
            return RED;
        }
    }

    private int getPLRColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && mc.player.getDistance(entity) < 5.5F) {
            return GREEN;
        } else {
            return RED;
        }
    }

    private int getLBYColor() {
        EntityPlayer entity = EntityUtil.getClosestEnemy();
        if (entity != null && mc.player.getDistance(entity) < 8.5F && EntityUtil.isPlayerSafe(entity)) {
            return GREEN;
        } else {
            return RED;
        }
    }

    private int getTotemColor(int totems) {
        if (totems > 0) {
            return GREEN;
        }

        return RED;
    }

    private int getPingColor() {
        if (NetworkUtil.getLatency() > 150) {
            return RED;
        }

        return GREEN;
    }

    /*
    protected boolean announcePotions() {
        return potions.getValue();
    }
    */

    protected boolean pearls() {
        return pearls.getValue();
    }
}