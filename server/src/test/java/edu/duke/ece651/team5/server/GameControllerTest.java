package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.server.MyEnum.GameStatus;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

public class GameControllerTest {
    @Test
    void testCreatePlayers() {
        // call create players inside it
        GameController gameController = new GameController(3);
        ArrayList<Player> players = gameController.getPlayers();
        assertEquals(3, players.size());
        assertEquals("Green", players.get(0).getName());
        assertEquals("Blue", players.get(1).getName());
        assertEquals("Red", players.get(2).getName());
    }

    @Test
    void testGetID() {
        GameController game1 = new GameController(2);
        GameController game2 = new GameController(3);

        // assertEquals(1, game1.getID());
        assertEquals(game1.getID() + 1, game2.getID());
    }

    @Test
    void testGetStatus() {
        GameController gameController = new GameController(3);
        assertEquals(GameStatus.WAITING, gameController.getStatus());
    }

    @Test
    void testJoinGame() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);
        User user4 = mock(User.class);
        String msg1 = gameController.joinGame(user1);
        String msg2 = gameController.joinGame(user2);
        String msg3 = gameController.joinGame(user3);
        String msg4 = gameController.joinGame(user4);

        assertNull(msg1);
        assertNull(msg2);
        assertEquals("Start", msg3);
        assertEquals("Full", msg4);
    }

    @Test
    void testKickUserOut() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        gameController.joinGame(user1);
        gameController.joinGame(user2);
        gameController.joinGame(user3);

        assertTrue(gameController.getPlayerToUserMap().containsKey("Blue"));
        gameController.kickUserOut(user2);
        assertFalse(gameController.getPlayerToUserMap().containsKey("Blue"));
    }

    @Test
    void testPauseGame() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        gameController.joinGame(user1);
        assertEquals(GameStatus.WAITING, gameController.getStatus());

        gameController.joinGame(user2);
        assertEquals(GameStatus.WAITING, gameController.getStatus());

        gameController.joinGame(user3);
        assertEquals(GameStatus.INITIALIZING, gameController.getStatus());

        gameController.pauseGame(user2);
        assertEquals(GameStatus.PAUSED, gameController.getStatus());

        assertFalse(gameController.getUserActiveStatus(user2));
        assertTrue(gameController.getUserActiveStatus(user1));
        assertTrue(gameController.getUserActiveStatus(user3));

    }

    @Test
    void testContinueGame() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        gameController.joinGame(user1);
        gameController.joinGame(user2);
        gameController.joinGame(user3);

        gameController.pauseGame(user2);
        gameController.pauseGame(user1);

        assertEquals(GameStatus.PAUSED, gameController.getStatus());
        assertFalse(gameController.continueGame(user2));
        assertEquals(GameStatus.PAUSED, gameController.getStatus());
        assertTrue(gameController.continueGame(user1));
        assertEquals(GameStatus.INITIALIZING, gameController.getStatus());
    }

    @Test
    void testGetUserActiveStatus() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        gameController.joinGame(user1);
        assertTrue(gameController.getUserActiveStatus(user1));
        gameController.joinGame(user2);
        assertTrue(gameController.getUserActiveStatus(user2));
        gameController.joinGame(user3);
        assertTrue(gameController.getUserActiveStatus(user3));

        gameController.pauseGame(user3);
        assertFalse(gameController.getUserActiveStatus(user3));
        gameController.continueGame(user3);
        assertTrue(gameController.getUserActiveStatus(user3));
    }

    @Test
    void testAssignTerritories() {
        GameController game = new GameController(3);
        game.assignTerritories(3);
        ArrayList<Territory> greenTerritories = (ArrayList<Territory>) game.getGame().getPlayeryByName("Green")
                .getTerritories();
        ArrayList<Territory> blueTerritories = (ArrayList<Territory>) game.getGame().getPlayeryByName("Blue")
                .getTerritories();
        ArrayList<Territory> redTerritories = (ArrayList<Territory>) game.getGame().getPlayeryByName("Red")
                .getTerritories();

        ArrayList<Integer> greenTerriIDs = new ArrayList<>();
        for (int i = 0; i < greenTerritories.size(); ++i) {
            greenTerriIDs.add(greenTerritories.get(i).getId());
        }

        ArrayList<Integer> blueTerriIDs = new ArrayList<>();
        for (int i = 0; i < blueTerritories.size(); ++i) {
            blueTerriIDs.add(blueTerritories.get(i).getId());
        }

        ArrayList<Integer> redTerriIDs = new ArrayList<>();
        for (int i = 0; i < redTerritories.size(); ++i) {
            redTerriIDs.add(redTerritories.get(i).getId());
        }

        ArrayList<Integer> greenExpected = new ArrayList<>(
                Arrays.asList(0, 3, 6, 9, 12, 15, 18, 21));

        ArrayList<Integer> blueExpected = new ArrayList<>(
                Arrays.asList(1, 4, 7, 10, 13, 16, 19, 22));

        ArrayList<Integer> redExpected = new ArrayList<>(
                Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23));

        assertEquals(greenExpected, greenTerriIDs);
        assertEquals(blueExpected, blueTerriIDs);
        assertEquals(redExpected, redTerriIDs);
    }

    @Test
    void testInitializeGame(){
        GameController game = new GameController(3);
        game.assignTerritories(3);
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        User mockUser3 = mock(User.class);

        HashMap<String, Integer> unitPlacements1 = new HashMap<>();
        unitPlacements1.put("Narnia", 5);
        unitPlacements1.put("Scadrial", 6);
        unitPlacements1.put("Gondor", 7);

        HashMap<String, Integer> unitPlacements2 = new HashMap<>();
        unitPlacements1.put("Elantris", 5);
        unitPlacements1.put("Oz", 6);
        unitPlacements1.put("Mordor", 7);

        HashMap<String, Integer> unitPlacements3 = new HashMap<>();
        unitPlacements1.put("Midkemia", 5);
        unitPlacements1.put("Roshar", 6);
        unitPlacements1.put("Hogwarts", 7);

        String error1 = game.initializeGame(mockUser1, unitPlacements1);
        assertEquals("Cannot initialize", error1);

        game.joinGame(mockUser1);
        game.joinGame(mockUser2);
        game.joinGame(mockUser3);

        String success1 = game.initializeGame(mockUser1, unitPlacements1);
        assertEquals("Placement succeeded", success1);
        String success2 = game.initializeGame(mockUser2, unitPlacements2);
        assertEquals("Placement succeeded", success2);
        String success3 = game.initializeGame(mockUser3, unitPlacements3);
        assertEquals("Placement finished", success3);
    }

    @Test
    void testReceiveActionFromUser() throws ClassNotFoundException, IOException{
        GameController game = new GameController(3);
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        User mockUser3 = mock(User.class);

        game.joinGame(mockUser1);
        game.joinGame(mockUser2);
        game.joinGame(mockUser3);

        Action action1 = new Action(new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>());
        String correctMsg = game.receiveActionFromUser(mockUser1, action1);
        assertNull(correctMsg);

        MoveOrder moveOrder = new MoveOrder("Narnia", "Elantris", new SoldierArmy(), new Player("Green"));
        ArrayList<MoveOrder> moveOrders = new ArrayList<>();
        moveOrders.add(moveOrder);
        Action action2 = new Action(new ArrayList<>(), moveOrders, null, new ArrayList<>());
        String errorMsg = game.receiveActionFromUser(mockUser2, action2);
        assertNotNull(errorMsg);
    }

    @Test
    void testTryResolveAllOrders(){
        //
    }
}
