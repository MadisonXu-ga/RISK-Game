package edu.duke.ece651.team5.server;

import edu.duke.ece651.team5.shared.Territory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class GameControllerTest {
  @Test
  void testAssignTerritories() {
    GameController gc = new GameController();
    assertEquals(0, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    gc.assignTerritories(3);
    assertEquals(3, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    ArrayList<Territory> terris = new ArrayList<>();
    
  }

  @Test
  void testGetPlayerName() {

  }

  @Test
  void testGetRiskMap() {

  }

  @Test
  void testResolveUnitPlacement() {

  }
}
