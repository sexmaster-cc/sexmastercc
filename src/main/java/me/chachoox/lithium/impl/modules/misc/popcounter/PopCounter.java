package me.chachoox.lithium.impl.modules.misc.popcounter;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.logger.Logger;
import me.chachoox.lithium.api.util.text.ColorEnum;

public class PopCounter extends Module {

    protected final Property<Boolean> watermark = new Property<>(
            true,
            new String[]{"Watermark", "wmark", "suffix"},
            "Puts [SexMaster.CC] in front of the message."
    );

    protected final Property<Boolean> ordinalNumbers =
            new Property<>(
                    false,
                    new String[]{"OrdinalNumbers", "ord", "num", "alternativenumber"},
                    "Uses ordinal numbers (1st, 2nd, 3rd) instead of Cardinal numbers (1, 2, 3)"
            );

    protected final Property<Boolean> clearOnLogout =
            new Property<>(false,
                    new String[]{"ClearOnLogout", "Clearonlog", "logclear"},
                    "Clears players totem pops if they log out."
            );

    protected final Property<Boolean> clearOnVisRange =
            new Property<>(
                    false,
                    new String[]{"ClearOnVisRange", "visualrange", "visrange", "renderdistance"},
                    "Clears players totem pops if they leave our render distance."
            );

    protected final EnumProperty<ColorEnum> totemColor =
            new EnumProperty<>(
                    ColorEnum.LIGHTPURPLE,
                    new String[]{"TotemColor", "totcolor", "tcolor"},
                    "Changes the player name color in pop string, -> player (popped) their 5th (totem)!"
            );

    protected final EnumProperty<ColorEnum> numberColor =
            new EnumProperty<>(
                    ColorEnum.RED,
                    new String[]{"NumberColor", "numcolor", "numbcolor", "ncolor"},
                    "Changes the number in the pop string, -> player popped their (5th) totem!"
            );

    protected final EnumProperty<ColorEnum> finalColor =
            new EnumProperty<>(
                    ColorEnum.GREEN,
                    new String[]{"FinalColor", "finalcol", "lastcolor"},
                    "Changes the death number color, -> player died after popping their (5th) totem!"
            );

    protected final EnumProperty<ColorEnum> plrColor =
            new EnumProperty<>(
                    ColorEnum.LIGHTPURPLE,
                    new String[]{"PlayerColor", "playercol", "plyrcol"},
                    "Changes the player name color in pop string, -> (player) popped their 5th totem!"
            );

    protected final EnumProperty<ColorEnum> friendColor =
            new EnumProperty<>(
                    ColorEnum.AQUA,
                    new String[]{"FriendColor", "frdcolor", "friendcol"},
                    "Changes the friend color in the pop message, -> (friend) popped their 5th totem!"
            );

    public PopCounter() {
        super("PopCounter", new String[]{"PopCounter", "totempopcounter", "popcount"}, "Counts players totempops.", Category.MISC);
        this.offerListeners(new ListenerPop(this), new ListenerDeath(this));
        this.offerProperties(watermark, ordinalNumbers, clearOnLogout, clearOnVisRange, totemColor, numberColor, finalColor, plrColor, friendColor);
    }

    protected String getNumberStringThing(int pops) {
        if (pops == 1) {
            return "st";
        }
        if (pops == 2) {
            return "nd";
        }
        if (pops == 3) {
            return "rd";
        }
        if (pops >= 4 && pops < 21) {
            return "th";
        }
        int lastDigit = pops % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        if (lastDigit == 3) {
            return "rd";
        }
        return "th";
    }

    protected void sendMessage(String msg, int id) {
        if (watermark.getValue()) {
            Logger.getLogger().log(msg, id);
        } else {
            Logger.getLogger().logNoMark(msg, id);
        }
    }

    protected String getTotemColor() {
        return (totemColor.getValue().getColor());
    }

    protected String getNumberColor() {
        return (numberColor.getValue().getColor());
    }

    protected String getPlayerColor() {
        return (plrColor.getValue().getColor());
    }

    protected String getFriendColor() {
        return (friendColor.getValue().getColor());
    }

    protected String getFinalColor() {
        return (finalColor.getValue().getColor());
    }

    public boolean clearOnLog() {
        return isEnabled() && clearOnLogout.getValue();
    }

    public boolean clearOnVisualRange() {
        return isEnabled() && clearOnVisRange.getValue();
    }
}
