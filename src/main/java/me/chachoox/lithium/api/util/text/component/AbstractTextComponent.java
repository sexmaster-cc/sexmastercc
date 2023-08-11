package me.chachoox.lithium.api.util.text.component;

import net.minecraft.util.text.TextComponentString;

@SuppressWarnings("NullableProblems")
public abstract class AbstractTextComponent extends TextComponentString {

    public static final AbstractTextComponent EMPTY = new AbstractTextComponent("") {
        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getUnformattedComponentText() {
            return "";
        }

        @Override
        public TextComponentString createCopy() {
            return EMPTY;
        }
    };


    public AbstractTextComponent(String initial) {
        super(initial);
    }


    @Override
    public abstract String getText();

    @Override
    public abstract String getUnformattedComponentText();

    @Override
    public abstract TextComponentString createCopy();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AbstractTextComponent)) {
            return false;
        } else {
            return this.getText().equals(((AbstractTextComponent) o).getText());
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "CustomComponent{text='" + this.getText() + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

}

