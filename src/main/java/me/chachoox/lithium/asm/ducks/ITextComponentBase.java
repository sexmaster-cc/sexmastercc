package me.chachoox.lithium.asm.ducks;

import net.minecraft.util.text.ITextComponent;

import java.util.function.Supplier;

public interface ITextComponentBase {
    void setFormattingHook(Supplier<String> hook);

    void setUnFormattedHook(Supplier<String> hook);

    ITextComponent copyNoSiblings();
}
