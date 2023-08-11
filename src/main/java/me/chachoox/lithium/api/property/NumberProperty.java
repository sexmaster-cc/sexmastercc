package me.chachoox.lithium.api.property;

//important: properties that are not integer need to use steps!
public class NumberProperty<T extends Number> extends Property<T> {
    private final T minimum;
    private final T maximum;
    private T steps;
    private final boolean clamp;

    public NumberProperty(T value, T minimum, T maximum, String[] aliases, String description) {
        super(value, aliases, description);
        clamp = true;
        this.minimum = minimum;
        this.maximum = maximum;;
    }

    public NumberProperty(T value, T minimum, T maximum, T steps, String[] aliases, String description) {
        super(value, aliases, description);
        clamp = true;
        this.minimum = minimum;
        this.maximum = maximum;
        this.steps = steps;
    }

    public NumberProperty(T value, String[] aliases, String desc) {
        super(value, aliases, desc);
        clamp = false;
        maximum = null;
        minimum = null;
        steps = null;
    }

    public T getMaximum() {
        return maximum;
    }

    public T getMinimum() {
        return minimum;
    }

    public T getSteps() {
        return steps;
    }

    @Override
    public void setValue(T value) {
        if (clamp & (maximum != null && minimum != null)) {
            if (value instanceof Integer) {
                if (value.intValue() > maximum.intValue()) {
                    value = maximum;
                } else if (value.intValue() < minimum.intValue()) {
                    value = minimum;
                }
            } else if (value instanceof Float) {
                if (value.floatValue() > maximum.floatValue()) {
                    value = maximum;
                } else if (value.floatValue() < minimum.floatValue()) {
                    value = minimum;
                }
            } else if (value instanceof Double) {
                if (value.doubleValue() > maximum.doubleValue()) {
                    value = maximum;
                } else if (value.doubleValue() < minimum.doubleValue()) {
                    value = minimum;
                }
            } else if (value instanceof Long) {
                if (value.longValue() > maximum.longValue()) {
                    value = maximum;
                } else if (value.longValue() < minimum.longValue()) {
                    value = minimum;
                }
            } else if (value instanceof Short) {
                if (value.shortValue() > maximum.shortValue()) {
                    value = maximum;
                } else if (value.shortValue() < minimum.shortValue()) {
                    value = minimum;
                }
            } else if (value instanceof Byte) {
                if (value.byteValue() > maximum.byteValue()) {
                    value = maximum;
                } else if (value.byteValue() < minimum.byteValue()) {
                    value = minimum;
                }
            }
        }
        super.setValue(value);
    }
}

