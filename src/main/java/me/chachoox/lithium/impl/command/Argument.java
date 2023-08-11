package me.chachoox.lithium.impl.command;

import me.chachoox.lithium.api.interfaces.Labeled;

public class Argument implements Labeled {
    private final String label;
    private String value;
    private boolean present = false;

    public Argument(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isPresent() {
        return this.present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}