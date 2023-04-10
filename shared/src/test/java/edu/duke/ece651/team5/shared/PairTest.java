package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {
    Pair<Integer, Integer> pair = new Pair<>(10, 20);

    @Test
    void getFirst() {
        assertEquals(10, pair.getFirst());
    }

    @Test
    void getSecond() {
        assertEquals(20, pair.getSecond());
    }

    @Test
    void testEquals() {
        assertEquals(pair, pair);
        assertNotEquals(pair, null);
        Pair<Integer, Integer> pair2 = new Pair<>(10, 20);
        assertEquals(pair, pair2);
        Pair<Integer, Integer> pair3 = new Pair<>(10, 10);
        Pair<Integer, Integer> pair4 = new Pair<>(5, 20);
        assertNotEquals(pair, pair3);
        assertNotEquals(pair, pair4);
    }

    @Test
    void testHashcode() {
        Pair<Integer, Integer> pairNull = new Pair<>(null, null);
        assertEquals(0, pairNull.hashCode());
    }


}
