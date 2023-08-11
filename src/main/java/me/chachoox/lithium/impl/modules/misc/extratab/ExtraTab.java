package me.chachoox.lithium.impl.modules.misc.extratab;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.EnumProperty;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.text.ColorEnum;
import me.chachoox.lithium.impl.managers.Managers;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class ExtraTab extends Module {

    public final NumberProperty<Integer> tabSize =
            new NumberProperty<>(
                    80, 1, 1000,
                    new String[]{"TabSize", "size", "tablength"},
                    "Overrides the minecraft tab size and replaces it for this."
            );

    public final EnumProperty<ColorEnum> friendColor =
            new EnumProperty<>(
                    ColorEnum.AQUA,
                    new String[]{"FriendColor", "frdcolor", "friend"},
                    "If we have someone added we will change their name colour to this on tab."
            );

    public final Property<Boolean> ping =
            new Property<>(
                    false,
                    new String[]{"Ping", "pingington", "pin"},
                    "Displays ping in text instead of bars."
            );

    public final Property<Boolean> bars =
            new Property<>(
                    true,
                    new String[]{"Bars", "vanillabars", "bar"},
                    "Displays the vanilla ping bars."
            );

    public ExtraTab() {
        super("ExtraTab", new String[]{"ExtraTab", "tabtweaks", "tab"}, "Tweaks how tab works.", Category.MISC);
        this.offerProperties(tabSize, friendColor, ping, bars);
    }

    public String getName(NetworkPlayerInfo info) {
        String name = info.getDisplayName() != null ? info.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());

        if (Managers.FRIEND.isFriend(name)) {
            return friendColor.getValue().getColor() + name;
        }

        return name;
    }

}
