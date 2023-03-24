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
  @Mock private RISKMap mockMap;
  
  @Test
  void testCheckAttackOrderUnitValid() {
    RISKMap testMap = spy(new RISKMap("test_map_config.txt"));
    when(testMap.getTerritoryByName(Mockito.isA(String.class))).thenReturn(10);


  }

  @Test
  void testCheckMoveOrderUnitValid() {

  }

  @Test
  void testCheckUnitValid() {
    RISKMap testMap = spy(new RISKMap("test_map_config.txt"));
    when(testMap.getAvailableUnit()).thenReturn(50);
    UnitValidRuleChecker checker = new UnitValidRuleChecker();
    assertEquals(true, checker.checkUnitValid(testMap, createPlaceInfo()));
    
  }

  private ArrayList<Player> createplayers(){
    return new ArrayList<>(Arrays.asList(
      new Player("Green"),
      new Player("Blue")));
  }

  private HashMap<String, Integer> createPlaceInfo(){
    HashMap<String, Integer> placeInfo = new HashMap<>();
    placeInfo.put("Narnia", 10);
    placeInfo.put("Elantris", 40);
    //placeInfo.put("Midkemia", 30);
    return placeInfo;
  }

  private RISKMap createTestMap(){
    return new RISKMap("test_map_config.txt");
  }

  private void initMap(RISKMap map, int numPlayers){
    ArrayList<Player> playerNames = createplayers();
    map.initPlayers(playerNames);
    ArrayList<String> terriName = new ArrayList<>(Arrays.asList(
            "Narnia", "Elantris", "Midkemia", "Scadrial", "Oz", "Roshar"));
    int numTerritories = terriName.size();
    for (int i = 0; i < numTerritories; ++i) {
        Player p = map.getPlayerByName(playerNames.get(i % numPlayers));
        String territoryName = terriName.get(i);
        Territory territory = riskMap.getTerritoryByName(territoryName);
        p.addTerritory(territory);
        territory.setOwner(p);
    }
  }
  }
}
