package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.game.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class JoinableGamesController extends GoBackController {

    public Client client;
    @FXML
    AnchorPane multipleGameButtons;
    private Button[] gameButtons;
    private Game game;

    @FXML
    public void initialize() throws ClassNotFoundException, IOException {
        gameButtons = multipleGameButtons.lookupAll(".button").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {
            // System.out.println(gameButtons[i].getId());
            // Set the visibility of the button based on the corresponding boolean value
            if (gameButtons[i].getId().contains("Game")) {
                gameButtons[i].setVisible(false);
            }
        }

    }

    public JoinableGamesController(Client client) {
        this.client = client;
    }

    public void showJoinableGames(ArrayList<Integer> gameIDs) {

        System.out.println(gameButtons.length);
        for (int i = 0; i < gameIDs.size(); i++) {

            gameButtons[i].setVisible(true);

            System.out.println("the size of the gameID array is:" + gameIDs.size());

            if (gameIDs.size() > 0) {
                gameButtons[i].setText(gameIDs.get(i).toString());
            }
        }
    }

    public void goToWaitingScreen(String color) throws IOException, ClassNotFoundException {
        URL xmlResource = getClass().getResource("/waiting-room.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        // controllers.put(GoBackController.class, new GoBackController());
        controllers.put(JoinableGamesController.class, new JoinableGamesController(client));
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        multipleGamesController.setName(color);
        client.setColor(color);
        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waitin-room", scene);

        App.getPrimaryStage().setScene(scene);
        System.out.print("waiting");
        // Game game = client.getGame();

        // Game game = client.getGame();

        // Platform.runLater(() -> {
        // // code to be executed after the scene is set
        // try {
        // Game game = client.getGame();
        // goToPlacement(game);
        // } catch (ClassNotFoundException | IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // });

        // if (client.confirmGame().equals("Game sent")) {

        // goToPlacement(game);
        // }

    }

    // public void onsaveAndExit(ActionEvent ae) throws ClassNotFoundException,
    // IOException {

    // // TODO create the disconnect action with server

    // App.loadScenefromMain("multiple-games");
    // }

    public void onJoinGame(ActionEvent ae) throws ClassNotFoundException, IOException {

        Object source = ae.getSource();

        System.out.println("in Join function");
        if (source instanceof Button) {
            Button btn = (Button) source;
            // btn.setText("clicked");
            System.out.println("button pressed");
            Integer gameID = Integer.parseInt(btn.getText());
            String msg = client.joinNewGame(gameID);

            if (msg.equals("Joined Success")) {

                MultipleGamesController multipleGamesController = (MultipleGamesController) App
                        .loadController("multiple-games");

                String color = client.receiveColor();
                goToWaitingScreen(color);
                Game game = client.getGame();
                client.getGameConfirmation();
                goToPlacement(game);

                // multipleGamesController.refresh();

                // Game game = client.getGame();

                System.out.println("we received a game!" + gameID);
            }
            System.out.println(msg);

        }
    }

    public void goToPlacement(Game game) throws IOException, ClassNotFoundException {
        URL xmlResource = getClass().getResource("/mapPlacement.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        // controllers.put(GoBackController.class, new GoBackController());
        controllers.put(MapPlacementTerritory.class, new MapPlacementTerritory(client, game));
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        // App.addScenetoMain("pl", scene);

        App.getPrimaryStage().setScene(scene);
        // Game game = client.getGame();
        // System.out.println("we received a game!");
    }

}
