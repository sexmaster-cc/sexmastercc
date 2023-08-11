package me.chachoox.lithium.api.interfaces;

import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;

public interface Loggable {
    void log(String text);

    void log(String text, int id);

    void log(String text, boolean delete);

    void log(ITextComponent component, boolean delete);

    void log(Level level, String text);

    void logNoMark(String text);

    void logNoMark(String text, int id);

    void logNoMark(String text, boolean delete);

    void logNoMark(ITextComponent component, boolean delete);
}
