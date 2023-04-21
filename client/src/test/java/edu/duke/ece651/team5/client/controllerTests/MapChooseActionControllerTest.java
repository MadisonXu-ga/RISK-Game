package edu.duke.ece651.team5.client.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.client.controller.MapChooseActionController;
import edu.duke.ece651.team5.client.controller.MultipleGamesController;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class MapChooseActionControllerTest extends ApplicationTest {

    private Client mockClient;
    private Game mockGame;
    private Player mockPlayer;
    private Player mockPlayer2;
    private Territory mockTerritory;
    private Territory mockTerritory2;
    private RISKMap map;

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        // Create mock objects
        mockClient = Mockito.mock(Client.class);
        mockGame = Mockito.mock(Game.class);
        mockPlayer = Mockito.mock(Player.class);
        mockTerritory = Mockito.mock(Territory.class);
        mockTerritory2 = Mockito.mock(Territory.class);
        map = Mockito.mock(RISKMap.class);

        // Set up the behavior for the mock objects
        Mockito.when(mockClient.getGame()).thenReturn(mockGame, mockGame);
        Mockito.when(mockClient.getColor()).thenReturn("Blue", "Blue");
        Mockito.when(mockGame.getPlayerByName("Blue")).thenReturn(mockPlayer, mockPlayer);
        Mockito.when(mockPlayer.getTerritories())
                .thenReturn(new ArrayList<>(Arrays.asList(mockTerritory, mockTerritory)));
        Mockito.when(mockTerritory.getName()).thenReturn("Narnia", "Oz", "Narnia", "Oz");
        Mockito.when(mockTerritory2.getName()).thenReturn("Narnia");
        Mockito.when(mockClient.recvUpdatedGame()).thenReturn(mockGame);
        Mockito.when(mockGame.getMap()).thenReturn(map);
        Mockito.when(map.getTerritoryByName(any(String.class))).thenReturn(mockTerritory);
        Mockito.when(mockTerritory.getOwner()).thenReturn(mockPlayer);
        Mockito.when(mockTerritory.getSoldierArmy()).thenReturn(new SoldierArmy());

        URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);
        loader.setControllerFactory(c -> {
            if (c == MapChooseActionController.class) {
                return new MapChooseActionController(mockClient, mockGame);
            }
            if (c == MultipleGamesController.class) {
                return new MultipleGamesController(mockClient);
            }
            return null;
        });

        // Load the scene
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testGoToPlacement(FxRobot robot) {

        FxAssert.verifyThat("#upgradebtn", NodeMatchers.isVisible());
    }
}
