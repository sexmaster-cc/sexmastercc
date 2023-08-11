package me.chachoox.lithium.api.util.text.component;

import me.chachoox.lithium.asm.ducks.ITextComponentBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.function.Supplier;

public class SuppliedComponent extends AbstractTextComponent {

    protected final Supplier<String> supplier;

    public SuppliedComponent(Supplier<String> supplier) {
        super(supplier.get());
        this.supplier = supplier;

        ((ITextComponentBase) this).setFormattingHook(new SimpleTextFormatHook(this));
        ((ITextComponentBase) this).setUnFormattedHook(new SimpleTextFormatHook(this));
    }

    @Override
    public String getText() {
        return supplier.get();
    }

    @Override
    public String getUnformattedComponentText() {
        return supplier.get();
    }

    @Override
    public TextComponentString createCopy() {
        SuppliedComponent copy = new SuppliedComponent(supplier);
        copy.setStyle(this.getStyle().createShallowCopy());

        for (ITextComponent component : this.getSiblings()) {
            copy.appendSibling(component.createCopy());
        }

        return copy;
    }

}

