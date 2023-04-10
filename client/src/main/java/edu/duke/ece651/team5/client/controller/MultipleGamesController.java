package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    public void initialize() throws ClassNotFoundException, IOException {
        gameButtons = multipleGameButtons.lookupAll(".button.neutralbtn").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {
            // System.out.println(gameButtons[i].getId());
            // Set the visibility of the button based on the corresponding boolean value

            gameButtons[i].setVisible(false);
        }

    }

    public void refresh() throws ClassNotFoundException, IOException {

        ArrayList<Integer> gameIDs = client.getGamesStarted();

        System.out.println("This is executing for size:" + gameIDs);
        for (int i = 0; i < gameIDs.size(); i++) {
            gameButtons[i].setVisible(true);
            gameButtons[i].setText(gameIDs.get(i).toString());
        }
    }

    public MultipleGamesController(Client client) {

        this.client = client;

    }

    public void onGame1(ActionEvent ae) {

        System.out.print(gameButtons.length);

    }

    public void onBeginNewGame(ActionEvent ae) throws IOException, ClassNotFoundException {
        String msg = client.beginNewGame();

        System.out.println(msg);
        goToWaitingScreen();

    }

    public void onjoinOtherGames(ActionEvent ae) throws IOException, ClassNotFoundException {
        // String msg = client.beginNewGame();
        ArrayList<Integer> gameIDs = client.getJoinableGames();
        seeJoinableGames(gameIDs);

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

    public void onLogOut() throws IOException {

        client.logOut();
        App.loadScenefromMain("login-page");
    }

    public void goToWaitingScreen() throws IOException {
        URL xmlResource = getClass().getResource("/waiting-room.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(GoBackController.class, new GoBackController());
        controllers.put(JoinableGamesController.class, new JoinableGamesController(client));
        controllers.put(MultipleGamesController.class, this);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waitin-room", scene);

        App.getPrimaryStage().setScene(scene);
    }

    public void seeJoinableGames(ArrayList<Integer> gameIDs) throws IOException, ClassNotFoundException {
        URL xmlResource = getClass().getResource("/joining-games.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        JoinableGamesController joinableGames = new JoinableGamesController(client);
        controllers.put(GoBackController.class, new GoBackController());
        controllers.put(JoinableGamesController.class, joinableGames);

        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waiting-room", scene);
        // App.addControllertoMain("waiting-room", joinableGames);
        joinableGames.showJoinableGames(gameIDs);
        App.getPrimaryStage().setScene(scene);
    }

    public Button[] getButtons() {
        return gameButtons;
    }

    public void showGamesStarted() throws ClassNotFoundException, IOException {

        ArrayList<Integer> gameIDs = client.getGamesStarted();
        System.out.println(gameIDs.size());
        for (int i = 0; i < gameIDs.size(); i++) {

            gameButtons[i].setVisible(true);

            // System.out.println("the size of the gameID array is:" + gameIDs.size());

            gameButtons[i].setText(gameIDs.get(i).toString());

        }
    }
}
