package me.chachoox.lithium.impl.modules.other.hud.util;

import me.chachoox.lithium.api.util.text.TextColor;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextRadarUtil {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getDistanceColor(float distance) {
        if (distance >= 25.0f) {
            return TextColor.GREEN;
        } else if (distance > 15.0f) {
            return TextColor.YELLOW;
        } else //noinspection ConstantConditions
            if (distance >= 50.0f) {
                return TextColor.GOLD;
            } else {
                return TextColor.RED;
            }
    }

    public static String getHealthColor(int health) {
        if (health >= 20) {
            return TextColor.GREEN;
        } else if (health >= 10) {
            return TextColor.YELLOW;
        } else if (health >= 5) {
            return TextColor.GOLD;
        } else {
            return TextColor.RED;
        }
    }
}
