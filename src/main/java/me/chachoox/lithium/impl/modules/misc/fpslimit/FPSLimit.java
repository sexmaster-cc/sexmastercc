package me.chachoox.lithium.impl.modules.misc.fpslimit;

import me.chachoox.lithium.api.module.Category;
import me.chachoox.lithium.api.module.Module;
import me.chachoox.lithium.api.property.NumberProperty;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.LambdaListener;

/**
 * @author moneymaker552
 */
public class FPSLimit extends Module {

    private final NumberProperty<Integer> tabbedFPS =
            new NumberProperty<>(
                    0, 0, 360,
                    new String[]{"TabbedFps", "fps"},
                    "What we limit the fps to when tabbed in, set to 0 if you want unused."
            );

    private final NumberProperty<Integer> unfocusedFPS =
            new NumberProperty<>(
                    0, 0, 60,
                    new String[]{"UnfocusedFps", "unfocusedcpu", "unfocusedfp"},
                    "What we limit the fps to when not tabbed in, set to 0 if you want unused."
            );

    private int frames;

    public FPSLimit() {
        super("FPSLimit", new String[]{"FPSLimit", "unfocusedfps", "betterframes"}, "Limits minecraft FPS.", Category.MISC);
        this.offerProperties(tabbedFPS, unfocusedFPS);
        this.tabbedFPS.addObserver(event -> {
            if (tabbedFPS.getValue() != 0) {
                frames = tabbedFPS.getValue();
            }
        });
        this.listeners.add(new LambdaListener<>(TickEvent.class, event -> {
            if (tabbedFPS.getValue() != 0) {
                mc.gameSettings.limitFramerate = frames;
            }
        }));
    }

    @Override
    public void onEnable() {
        if (tabbedFPS.getValue() != 0) {
            frames = tabbedFPS.getValue();
        }
    }

    public int getUnfocusedFPS() {
        return unfocusedFPS.getValue();
    }

    public int getFocusedFPS() {
        return tabbedFPS.getValue();
    }
}
