package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SoldierTest {
  @Test
  void testEqualsAndHash() {
    Soldier s1 = new Soldier();
    Soldier s2 = new Soldier();
    Soldier s3 = new Soldier();

    assertTrue(s1.equals(s2));
    assertTrue(s1.equals(s1));
    assertFalse(s1.equals(1));
    assertEquals(s1.hashCode(), s3.hashCode());

  }


}
