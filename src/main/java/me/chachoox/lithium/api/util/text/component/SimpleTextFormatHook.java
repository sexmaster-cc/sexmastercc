package me.chachoox.lithium.api.util.text.component;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

import java.util.function.Supplier;

public class SimpleTextFormatHook implements Supplier<String> {
    private final TextComponentBase base;

    public SimpleTextFormatHook(TextComponentBase base) {
        this.base = base;
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();

        for (ITextComponent component : base) {
            sb.append(component.getUnformattedComponentText());
        }

        return sb.toString();
    }

}