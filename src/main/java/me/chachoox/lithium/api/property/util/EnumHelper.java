package me.chachoox.lithium.api.property.util;

public class EnumHelper {

    public static Enum<?> fromString(Enum<?> initial, String name) {
        Enum<?> e = fromString(initial.getDeclaringClass(), name);
        if (e != null) {
            return e;
        }

        return initial;
    }

    public static <T extends Enum<?>> T fromString(Class<T> type, String name) {
        for (T constant : type.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                return constant;
            }
        }

        return null;
    }

}