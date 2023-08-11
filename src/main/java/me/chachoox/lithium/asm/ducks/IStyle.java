package me.chachoox.lithium.asm.ducks;

import net.minecraft.util.text.event.ClickEvent;

import java.util.function.Supplier;

public interface IStyle {
    void setRightClickEvent(ClickEvent event);

    void setMiddleClickEvent(ClickEvent event);

    ClickEvent getRightClickEvent();

    ClickEvent getMiddleClickEvent();

    void setSuppliedInsertion(Supplier<String> insertion);

    void setRightInsertion(Supplier<String> rightInsertion);

    void setMiddleInsertion(Supplier<String> middleInsertion);

    String getRightInsertion();

    String getMiddleInsertion();

}
