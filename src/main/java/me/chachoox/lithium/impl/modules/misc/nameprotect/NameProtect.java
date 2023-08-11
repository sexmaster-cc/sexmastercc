package me.chachoox.lithium.impl.modules.misc.nameprotect;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.property.StringProperty;

//TODO: custom skins
public class NameProtect extends Module {

    protected final StringProperty fakeName =
            new StringProperty(
                    "gamergobindyt",
                    new String[]{"Name", "FakeName", "TheName", "n"}
            );

    public final Property<Boolean> fakeSkin =
            new Property<>(
                    false,
                    new String[]{"SpoofSkin", "spoofedskin", "fakeskin", "steve", "alex"},
                    "Switches your skin to steve or alex."
            );

    public NameProtect() {
        super("NameProtect", new String[]{"NameProtect", "nickhider", "namespoof"}, "Spoofs your name and skin.", Category.MISC);
        this.offerProperties(fakeName, fakeSkin);
    }

    public String getName() {
        return fakeName.getValue();
    }

    public boolean isFakeSkin() {
        return isEnabled() && fakeSkin.getValue();
    }

}
