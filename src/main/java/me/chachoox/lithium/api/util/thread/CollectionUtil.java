package me.chachoox.lithium.api.util.thread;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtil {
    public static void emptyQueue(Queue<Runnable> runnables) {
        emptyQueue(runnables, Runnable::run);
    }

    public static <T> void emptyQueue(Queue<T> queue, Consumer<T> onPoll) {
        while (!queue.isEmpty()) {
            T polled = queue.poll();
            if (polled != null) {
                onPoll.accept(polled);
            }
        }
    }

    @SafeVarargs
    public static <T> List<List<T>> split(List<T> list, Predicate<T>...predicates) {
        List<List<T>> result = new ArrayList<>(predicates.length + 1);

        List<T> current = new ArrayList<>(list);
        List<T> next    = new ArrayList<>();
        for (Predicate<T> p : predicates) {
            Iterator<T> it = current.iterator();
            while (it.hasNext()) {
                T t = it.next();
                if (p.test(t)) {
                    next.add(t);
                    it.remove();
                }
            }

            result.add(next);
            next = new ArrayList<>();
        }

        result.add(current);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T, C extends T> C getByClass(Class<C> clazz, Collection<T> collection) {
        for (T t : collection) {
            if (clazz.isInstance(t)) {
                return (C) t;
            }
        }

        return null;
    }

    public static <T, R> List<T> convert(R[] array, Function<R, T> function) {
        List<T> result = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            result.add(i, function.apply(array[i]));
        }

        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <T> T getLast(Collection<T> iterable) {
        T last = null;
        for (T t : iterable) {
            last = t;
        }

        return last;
    }

}


