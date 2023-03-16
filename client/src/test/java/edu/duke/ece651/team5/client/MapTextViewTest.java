package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.RISKMap;

public class MapTextViewTest {
  @Test
  void testDisplayMap() {
    RISKMap map = new RISKMap();
    MapTextView view = new MapTextView(map);
    String res = view.displayMap();
    String expected = "Green Player: \n" + 
   "-------------10 units: in Narnia (next to: Elantris)\n";
    
    assertEquals(expected, res);

  }
}
