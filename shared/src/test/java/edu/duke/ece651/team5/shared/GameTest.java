package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.resource.WeatherType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Territory territory;
    private Game game;
    private RISKMap map;
    private Player player1 = new Player("Player 1");
    private Player player2 = new Player("Player 2");

    @BeforeEach
    private void setUp(){
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1"));
        territories.put("Territory 2", new Territory(2, "Territory 2"));
        territories.put("Territory 3", new Territory(3, "Territory 3"));
        territories.put("Territory 4", new Territory(4, "Territory 4"));

        HashMap<Integer, List<RISKMap.Edge>> connections = new HashMap<>();
        connections.put(1, Arrays.asList(new RISKMap.Edge(1, 2, 5),
                                         new RISKMap.Edge(1, 4, 1)));
        connections.put(2, Arrays.asList(new RISKMap.Edge(2, 1, 5),
                                         new RISKMap.Edge(2, 4, 3)));
        connections.put(3, Arrays.asList(new RISKMap.Edge(3, 4, 4)));
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1),
                                         new RISKMap.Edge(4, 2, 3),
                                         new RISKMap.Edge(4, 3, 4)));

        map = new RISKMap(territories, connections);
        
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        game = new Game(players, map);
    }

    @Test
    void testGetMap() {
        assertEquals(map, game.getMap());
    }

    @Test
    void testGetPlayers() {
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void testGetPlayeryByName() {
        assertEquals("Player 1", game.getPlayeryByName("Player 1").getName());
    }

    @Test
    void getAllianceSoldierArmy() {
        Territory territory1 = map.getTerritoryByName("Territory 1");
        SoldierArmy allianceSoliderArmy = new SoldierArmy();
        allianceSoliderArmy.addSoldier(new Soldier(SoldierLevel.CAVALRY), 2);
        territory1.addAllianceSoldier(allianceSoliderArmy);
        assertEquals(2, territory1.getAllianceSoliderArmy().getTotalCountSolider());
    }

    @Test
    void setGameID() {
        game.setGameID(1);
        assertEquals(1, game.getGameID());
    }

    @Test
    void getGameID() {
        game.setGameID(2);
        assertEquals(2, game.getGameID());
    }

    @Test
    void removeBreakUpAlliance() {
        Territory territory1 = map.getTerritoryByName("Territory 1");
        Territory territory2 = map.getTerritoryByName("Territory 2");
        Territory territory3 = map.getTerritoryByName("Territory 3");
        Territory territory4 = map.getTerritoryByName("Territory 4");
        territory1.setOwner(player1);
        territory2.setOwner(player1);
        territory3.setOwner(player2);
        territory4.setOwner(player2);
        player1.addTerritory(territory1);
        player1.addTerritory(territory2);
        player2.addTerritory(territory3);
        player2.addTerritory(territory4);
        SoldierArmy soldierArmy = new SoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.CAVALRY), 2);
        territory1.addAllianceSoldier(soldierArmy);
        player1.addAliance(player2);
        player2.addAliance(player1);
        assertEquals(2, territory1.getAllianceSoliderArmy().getTotalCountSolider());
        game.removeBreakUpAlliance(player1);
        game.removeBreakUpAlliance(player2);

        assertEquals(0, territory1.getAllianceSoliderArmy().getTotalCountSolider());
        assertEquals(2, territory4.getSoldierArmy().getTotalCountSolider());

    }

    @Test
    void handleWeather() {
        game.handleWeather();
        assertNotNull(game.getWeather());
        Territory.setWeather(WeatherType.CLOUDY);
    }

    @Test
    void hanldeEvent() {
        game.hanldeEvent();
        assertNotNull(game.getEvent());
    }
}
