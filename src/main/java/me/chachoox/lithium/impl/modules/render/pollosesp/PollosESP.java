package me.chachoox.lithium.impl.modules.render.pollosesp;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.Property;
import net.minecraft.util.ResourceLocation;

public class PollosESP extends Module {

    protected final Property<Boolean> seizure =
            new Property<>(
                    false,
                    new String[]{"Seizure", "aids", "cancer"},
                    "Pollos pov."
            );

    protected final Property<Boolean> pollosScreen =
            new Property<>(
                    false,
                    new String[]{"PollosScreen", "yup", "yo"},
                    "Pollos is everything."
            );

    protected final Property<Boolean> pollosNoise =
            new Property<>(
                    false,
                    new String[]{"PollosNoise", "pollossounds", "pollosinthemorning"},
                    "Cluck cluck cluck."
            );

    protected final Property<Boolean> pollosFps =
            new Property<>(
                    false,
                    new String[]{"PollosFps", "PollosSimulator"},
                    "Pollos simulation."
            );

    public static final ResourceLocation POLLOS = new ResourceLocation("lithium/textures/misc/pollos.png");

    public PollosESP() {
        super("PollosESP", new String[]{"PollosESP", "chickenesp", "gayesp"}, "The one and only.", Category.RENDER);
        this.offerProperties(seizure, pollosNoise, pollosFps, pollosScreen);
        this.offerListeners(new ListenerRender(this), new ListenerRender2D(this), new ListenerUpdate(this));
    }

    @Override
    public void onDisable() {
        mc.gameSettings.limitFramerate = 360;
    }

    public boolean isSeizure() {
        return isEnabled() && seizure.getValue();
    }
}
