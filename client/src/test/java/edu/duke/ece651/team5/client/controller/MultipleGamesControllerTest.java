package edu.duke.ece651.team5.client.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.client.controller.LoginInController;
import edu.duke.ece651.team5.client.controller.MultipleGamesController;
import edu.duke.ece651.team5.client.controller.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class MultipleGamesControllerTest {
    App a;
    Stage stage;
    Client client;
    MultipleGamesController multipleGamesController;

    @Start
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        this.client = Mockito.mock(Client.class);
        a = new App(client);
        a.start(stage);

        URL xmlResource = getClass().getResource("/multiple-games2.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);
        this.multipleGamesController = new MultipleGamesController(client);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(LoginInController.class, new LoginInController(client));
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Calculate the desired width and height as a percentage of the screen size
        double sceneWidth = screenWidth * 0.9;
        double sceneHeight = screenHeight * 0.9;

        System.out.println("w:" + sceneWidth + ", H: " + sceneHeight);

        Scene scene = new Scene(new StackPane(bp), sceneWidth, sceneHeight);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    // @Disabled
    @Test
    void testJoinableGamesVisibility(FxRobot robot) throws InterruptedException {

        // retrieve the buttons for the games that are active
        Button[] buttons = multipleGamesController.getButtons();
        // verify that the buttons for begin a new game as a host and
        // join other games are visible
        FxAssert.verifyThat("#beginNewGamebtn", NodeMatchers.isVisible());
        FxAssert.verifyThat("#joinOtherGamesbtn", NodeMatchers.isVisible());

        // since we don't have any active games when we first enter, they should all be
        // invisible
        for (Button gameButton : buttons) {

            FxAssert.verifyThat(gameButton, NodeMatchers.isInvisible());
        }
        // for now the amount of games that will be active is 6
        assertEquals(6, buttons.length);
        // robot.clickOn("#beginNewGamebtn");
        // robot.clickOn("#saveAndExit");
        // FxAssert.verifyThat("#saveAndExit", NodeMatchers.isVisible());

    }

}