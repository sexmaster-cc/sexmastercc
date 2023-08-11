package me.chachoox.lithium.impl.modules.other.colours;

import me.chachoox.lithium.api.event.bus.Listener;
import me.chachoox.lithium.api.event.bus.instance.Bus;
import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.PersistentModule;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.colors.ColorUtil;
import me.chachoox.lithium.api.util.colors.HSLColor;
import me.chachoox.lithium.impl.event.events.update.TickEvent;

import java.awt.*;

public class Colours extends PersistentModule {

    public final NumberProperty<Float> hue =
            new NumberProperty<>(
                    0F, 0F, 360F, 3F,
                    new String[]{"Hue", "huee"},
                    "Hue of the global color."
            );

    public final NumberProperty<Float> saturation =
            new NumberProperty<>(
                    100F, 0F, 100F, 1F,
                    new String[]{"Saturation", "satur"},
                    "Saturation of the global color."
            );

    public final NumberProperty<Float> lightness =
            new NumberProperty<>(
                    45F, 0F, 100F, 1F,
                    new String[]{"Lightness", "brightness"},
                    "Lightness of the global color."
            );

    public final NumberProperty<Float> friendHue =
            new NumberProperty<>(
                    180F, 0F, 360F, 3F,
                    new String[]{"FriendHue", "frdhue"}, "Hue of the global friendcolor."
            );

    public final NumberProperty<Float> friendSaturation =
            new NumberProperty<>(
                    100F, 0F, 100F, 1F,
                    new String[]{"FriendSaturation", "frdsatur"},
                    "Saturation of the global friend color."
            );

    public final NumberProperty<Float> friendLightness =
            new NumberProperty<>(
                    45F, 0F, 100F, 1F,
                    new String[]{"FriendLightness", "frdlightness", "friendbrightness", "frdbrightness"},
                    "Lightness of the global friend color."
            );

    private final Property<Boolean> friendRainbow =
            new Property<>(
                    false,
                    new String[]{"FriendRainbow", "frdrainbow"},
                    "Changes global friend color to a rainbow."
            );

    private final Property<Boolean> rainbow =
            new Property<>(
                    false,
                    new String[]{"Rainbow", "ranbow"},
                    "Sets the global color to a rainbow."
            );

    private final NumberProperty<Integer> speed =
            new NumberProperty<>(
                    50, 0, 100,
                    new String[]{"RainbowSpeed", "rainbowsped"},
                    "Speed of the rainbow."
            );

    public final NumberProperty<Float> factor =
            new NumberProperty<>(
                    1.0f, 0.0f, 5.0f, 0.1f,
                    new String[]{"RainbowHue", "rainbowfactor"},
                    "The length of the rainbow."
            );

    private float rainbowHue;

    private static Colours COLOURS;

    public Colours() {
        super("Colours", new String[]{"Colours", "colors", "color"}, "Global colours.", Category.OTHER);
        this.offerProperties(hue, saturation, lightness, friendHue, friendSaturation, friendLightness, friendRainbow, rainbow, speed, factor);
        this.offerListeners(new Listener<TickEvent>(TickEvent.class) {
            @Override
            public void call(TickEvent event) {
                update();
            }
        });
        COLOURS = this;
    }

    public static Colours get() {
        return COLOURS;
    }

    @Override
    public void onLoad() {
        setEnabled(true);
        Bus.EVENT_BUS.subscribe(this);
    }

    private void update() {
        if (speed.getValue() == 0) {
            return;
        }

        rainbowHue = (System.currentTimeMillis() % ((360 * speed.getValue())) / (360.0f * speed.getValue()) * 360);
    }

    public Color getColour() {
        return rainbow.getValue() ? getRainbow() : HSLColor.toRGB(hue.getValue(), saturation.getValue(), lightness.getValue());
    }

    public Color getFriendColour() {
        return friendRainbow.getValue() ? getRainbow() : HSLColor.toRGB(friendHue.getValue(), friendSaturation.getValue(), friendLightness.getValue());
    }

    public Color getColourCustomAlpha(int alpha) {
        return ColorUtil.changeAlpha(getColour(), alpha);
    }

    public Color getRainbow() {
        return HSLColor.toRGB(rainbowHue, saturation.getValue(), lightness.getValue());
    }

    public float getRainbowHue() {
        return rainbowHue;
    }

    public float getRainbowHueByPosition(double pos) {
        return (float) (rainbowHue - pos * 0.001f);
    }
}
