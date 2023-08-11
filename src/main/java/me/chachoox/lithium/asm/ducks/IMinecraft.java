package me.chachoox.lithium.asm.ducks;

import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
    int getRightClickDelay();

    void setRightClickDelay(int delay);

    void setLeftClickCounter(int count);

    Timer getTimer();

    void click(Click type);

    void setSession(Session session);

    enum Click {
        RIGHT,
        LEFT,
        MIDDLE
    }

}
