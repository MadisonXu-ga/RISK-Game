package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MapTextViewTest {
  @Test
  void testDisplayMap() {
    MapTextView view = new MapTextView();
    String res = view.displayMap();
    String expected = "Green Player: \n" + 
   "-------------10 units: in  Narnia (next to: Elantris)\n";
    
    assertEquals(expected, res);

  }
}
