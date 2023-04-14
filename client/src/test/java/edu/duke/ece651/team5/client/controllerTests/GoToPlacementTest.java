package edu.duke.ece651.team5.client.controllerTests;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.client.controller.MapPlacementTerritory;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GoToPlacementTest extends ApplicationTest {
    private Client mockClient;
    private Game mockGame;
    private Player mockPlayer;
    private Territory mockTerritory;
    private Territory mockTerritory2;

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        // Create mock objects
        mockClient = Mockito.mock(Client.class);
        mockGame = Mockito.mock(Game.class);
        mockPlayer = Mockito.mock(Player.class);
        mockTerritory = Mockito.mock(Territory.class);
        mockTerritory2 = Mockito.mock(Territory.class);

        // Set up the behavior for the mock objects
        Mockito.when(mockClient.getGame()).thenReturn(mockGame, mockGame);
        Mockito.when(mockClient.getColor()).thenReturn("Blue", "Blue");
        Mockito.when(mockGame.getPlayerByName("Blue")).thenReturn(mockPlayer, mockPlayer);
        Mockito.when(mockPlayer.getTerritories())
                .thenReturn(new ArrayList<>(Arrays.asList(mockTerritory, mockTerritory)));
        Mockito.when(mockTerritory.getName()).thenReturn("Narnia", "Oz", "Narnia", "Oz");
        Mockito.when(mockTerritory2.getName()).thenReturn("Narnia");

        // Load the FXML file
        URL xmlResource = getClass().getResource("/mapPlacement.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        // Set the controller factory with mock objects
        loader.setControllerFactory(c -> {
            if (c == MapPlacementTerritory.class) {
                return new MapPlacementTerritory(mockClient, mockGame);
            }
            return null;
        });

        // Load the scene
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testGoToPlacement() {

        // Verify that the mock objects are called as expected
        verify(mockClient, times(16)).getColor();
        verify(mockGame, times(13)).getPlayerByName("Blue");
        verify(mockPlayer, times(13)).getTerritories();
    }
}
