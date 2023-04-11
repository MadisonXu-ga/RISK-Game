package edu.duke.ece651.team5.shared.datastructure;

import java.util.Objects;

/**
 * data structure to hold to object together
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 */
public class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Getter for first element in pair
     * @return first element
     */
    public T getFirst() {
        return first;
    }

    /**
     * Getter for second element in pair
     * @return second element
     */
    public U getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!Objects.equals(first, pair.first)) return false;
        return Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}

