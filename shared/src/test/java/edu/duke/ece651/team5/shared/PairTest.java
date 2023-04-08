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
}
