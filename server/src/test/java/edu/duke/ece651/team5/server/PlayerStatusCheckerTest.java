package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.Player;
import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.Territory;

public class PlayerStatusCheckerTest {
    @Test
    void testGetPlayerStatus() {
        RISKMap mockRiskMap = mock(RISKMap.class);
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        ArrayList<Player> players = new ArrayList<>();
        players.add(mockPlayer1);
        players.add(mockPlayer2);
        players.add(mockPlayer3);

        // Create some mock Territory objects
        Territory mockTerritory1 = mock(Territory.class);
        Territory mockTerritory2 = mock(Territory.class);
        Territory mockTerritory3 = mock(Territory.class);

        ArrayList<Territory> allTerritories = new ArrayList<>();
        allTerritories.add(mockTerritory1);
        allTerritories.add(mockTerritory2);
        allTerritories.add(mockTerritory3);

        ArrayList<Territory> territories1 = new ArrayList<>();
        territories1.add(mockTerritory1);
        territories1.add(mockTerritory3);
        ArrayList<Territory> territories2 = new ArrayList<>();
        territories2.add(mockTerritory2);
        ArrayList<Territory> territories3 = new ArrayList<>();

        when(mockRiskMap.getPlayers()).thenReturn(players);
        when(mockRiskMap.getTerritories()).thenReturn(allTerritories);
        when(mockPlayer1.getName()).thenReturn("Player1");
        when(mockPlayer2.getName()).thenReturn("Player2");
        when(mockPlayer3.getName()).thenReturn("Player3");
        when(mockPlayer1.getTerritories()).thenReturn(territories1);
        when(mockPlayer2.getTerritories()).thenReturn(territories2);
        when(mockPlayer3.getTerritories()).thenReturn(territories3);

        // Create the expected player status map
        HashMap<String, Boolean> expectedPlayerStatus1 = new HashMap<>();
        expectedPlayerStatus1.put("Player1", null);
        expectedPlayerStatus1.put("Player2", null);
        expectedPlayerStatus1.put("Player3", false);

        PlayerStatusChecker p = new PlayerStatusChecker();

        HashMap<String, Boolean> actualPlayerStatus1 = p.getPlayerStatus(mockRiskMap);

        // Verify the results
        assertEquals(expectedPlayerStatus1, actualPlayerStatus1);

        // test when there is a player win
        territories2.remove(mockTerritory2);
        territories1.add(mockTerritory2);

        HashMap<String, Boolean> expectedPlayerStatus2 = new HashMap<>();
        expectedPlayerStatus2.put("Player1", true);
        expectedPlayerStatus2.put("Player2", false);
        expectedPlayerStatus2.put("Player3", false);

        HashMap<String, Boolean> actualPlayerStatus2 = p.getPlayerStatus(mockRiskMap);
        assertEquals(expectedPlayerStatus2, actualPlayerStatus2);
    }

    @Test
    void testCheckWin(){
        // return null case
        HashMap<String, Boolean> playerStatus = new HashMap<>();
        playerStatus.put("player1", null);
        playerStatus.put("player2", false);
        playerStatus.put("player3", null);

        PlayerStatusChecker p = new PlayerStatusChecker();
        assertNull(p.checkWin(playerStatus));

        // return name case
        playerStatus.put("player3", true);
        assertEquals("player3", p.checkWin(playerStatus));
    }
}