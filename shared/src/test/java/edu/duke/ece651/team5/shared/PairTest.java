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
        assertNotEquals(pair, pair3);
    }


}
