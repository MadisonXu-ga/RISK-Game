package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MultipleGamesController extends GoBackController {

    @FXML
    AnchorPane multipleGameButtons;

    @FXML
    Button beginNewGame;
    @FXML
    Button joinOtherGames;

    private Button[] gameButtons;
    public Client client;

    @FXML
    public void initialize() {
        gameButtons = multipleGameButtons.lookupAll(".button.neutralbtn").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {
            System.out.println(gameButtons[i].getId());
            // Set the visibility of the button based on the corresponding boolean value

            gameButtons[i].setVisible(false);
        }
    }

    public MultipleGamesController(Client client) {

        this.client = client;

    }

    public void onGame1(ActionEvent ae) {

        System.out.print(gameButtons.length);

    }

    public void onBeginNewGame(ActionEvent ae) throws IOException {
        String msg = client.beginNewGame();

        if (msg.equals("game created")) {

            System.out.println(msg);
            goToWaitingScreen();
        }

    }

    public void onjoinOtherGames(ActionEvent ae) throws IOException {
        // String msg = client.beginNewGame();
        seeJoinableGames();

        // if (msg.equals("game created")) {

        // System.out.println(msg);
        // goToWaitingScreen();
        // }

    }

    public void onNewGame(ActionEvent ae) throws IOException {

        System.out.println("fake new game was pressed");

        URL xmlResource = getClass().getResource("/mapGoBack.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapGoBackController.class, new MapGoBackController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));

        App.getPrimaryStage().setScene(scene);

    }

    public void goToWaitingScreen() throws IOException {
        URL xmlResource = getClass().getResource("/waiting-room.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        // controllers.put(GoBackController.class, new GoBackController());
        controllers.put(MultipleGamesController.class, this);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waitin-room", scene);

        App.getPrimaryStage().setScene(scene);
    }

    public void seeJoinableGames() throws IOException {
        URL xmlResource = getClass().getResource("/joining-games.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapGoBackController.class, new MapGoBackController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waiting-room", scene);

        App.getPrimaryStage().setScene(scene);
    }

    public Button[] getButtons() {
        return gameButtons;
    }
}
