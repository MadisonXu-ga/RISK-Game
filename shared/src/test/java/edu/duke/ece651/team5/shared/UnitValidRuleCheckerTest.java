package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UnitValidRuleCheckerTest {
  @Mock
  private RISKMap mockMap;

  @Test
  void testCheckAttackOrderUnitValid() {
    RISKMap testMap = createTestMap();
    initMap(testMap, 2);

  }

  @Test
  void testCheckMoveOrderUnitValid() {

  }

  @Test
  void testCheckUnitValid() {
    RISKMap testMap = spy(new RISKMap("test_map_config.txt"));
    when(testMap.getAvailableUnit()).thenReturn(50);
    UnitValidRuleChecker checker = new UnitValidRuleChecker();
    assertEquals(true, checker.checkUnitValid(testMap, createPlaceInfo(10, 20, 20)));

    // wrong case: sum of units is not enough
    assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(1, 1, 1)));
    // wrong case: sum of units exceed
    assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 20, 20)));
    // wrong case: unit number < 0
    assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, -1, 10)));
    // wrong case: unit number == 0
    assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 0, 10)));
    // wrong case: unit number > max number
    assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 51, 10)));
  }

  private ArrayList<Player> createplayers() {
    return new ArrayList<>(Arrays.asList(
        new Player("Green"),
        new Player("Blue")));
  }

  private HashMap<String, Integer> createPlaceInfo(int num1, int num2, int num3) {
    HashMap<String, Integer> placeInfo = new HashMap<>();
    placeInfo.put("Narnia", num1);
    placeInfo.put("Elantris", num2);
    placeInfo.put("Midkemia", num3);

    return placeInfo;
  }

  private RISKMap createTestMap() {
    return new RISKMap("test_map_config.txt");
  }

  private void initMap(RISKMap map, int numPlayers) {
    ArrayList<Player> players = createplayers();
    map.initPlayers(players);
    ArrayList<String> terriName = new ArrayList<>(Arrays.asList(
        "Narnia", "Elantris", "Midkemia", "Scadrial", "Oz", "Roshar"));
    ArrayList<String> playerNames = new ArrayList<>(Arrays.asList("Green", "Blue"));
    int numTerritories = terriName.size();
    for (int i = 0; i < numTerritories; ++i) {
      Player p = map.getPlayerByName(playerNames.get(i % numPlayers));
      String territoryName = terriName.get(i);
      Territory territory = map.getTerritoryByName(territoryName);
      p.addTerritory(territory);
      territory.setOwner(p);
    }
  }

  private void initUnitPlacement(RISKMap map){
    game
    
  }
}
