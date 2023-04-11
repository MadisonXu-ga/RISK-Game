package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
