package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.game.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MultipleGamesController extends GoBackController {

    @FXML
    public AnchorPane multipleGameButtons;

    @FXML
    public Button beginNewGame;
    @FXML
    Button joinOtherGames;

    @FXML
    Text player1;

    @FXML
    TextField amountPlayers;

    public Button[] gameButtons;
    public Client client;

    @FXML
    public void initialize() throws ClassNotFoundException, IOException {
        gameButtons = multipleGameButtons.lookupAll(".button.neutralbtn").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {
            // System.out.println(gameButtons[i].getId());
            // Set the visibility of the button based on the corresponding boolean value

            gameButtons[i].setVisible(false);
        }
        System.out.println("This is executing for buttons:" + gameButtons.length);

    }

    /**
     * @param color
     *              sets the name of the player to textbox
     */
    public void setName(String color) {

        player1.setText("You are color:" + color);

    }

    public void refresh() throws ClassNotFoundException, IOException {

        ArrayList<Integer> gameIDs = client.getGamesStarted();
        // System.out.println(gameButtons);
        System.out.println("This is executing for size:" + gameIDs.size() + " " + gameIDs);
        System.out.println("This is executing for buttons:" + this.gameButtons.length);

        for (int i = 0; i < gameIDs.size(); i++) {
            // System.out.println(gameButtons[i].getText());
            gameButtons[i].setVisible(true);
            gameButtons[i].setText(gameIDs.get(i).toString());
        }
    }

    /**
     * @param client adds client to controller
     */
    public MultipleGamesController(Client client) {

        this.client = client;

    }

    /**
     * @param ae
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void Oncontinue(ActionEvent ae) throws ClassNotFoundException, IOException {

        Button source = (Button) ae.getSource();
        Integer gameIDInt = Integer.parseInt(source.getText());
        String message = client.continueGame(gameIDInt);
        Game retrievedGame = client.updatedGameAfterTurn();
        client.setGameID(gameIDInt);
        retrievedGame.setGameID(gameIDInt);

        if (message.equals("Initializing")) {
            goToPlacement(retrievedGame);
        }
        if (message.equals("Started")) {
            goToPlacingActionsScreen(retrievedGame);
        }
    }

    /**
     * @param ae
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void onBeginNewGame(ActionEvent ae) throws IOException, ClassNotFoundException {
        String playerAmountStr = amountPlayers.getText();
        Integer playeramount;
        try {
            playeramount = Integer.parseInt(playerAmountStr);
        } catch (NumberFormatException e) {
            playeramount = null;
        }
        amountPlayers.setText("");
        if (playeramount != null && playeramount > 0 && playeramount <= 4) {

            String msg = client.beginNewGame(playeramount);
            goToWaitingScreen();
        }

    }

    /**
     * @param ae
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void onjoinOtherGames(ActionEvent ae) throws IOException, ClassNotFoundException {
        // String msg = client.beginNewGame();
        ArrayList<Integer> gameIDs = client.getJoinableGames();
        seeJoinableGames(gameIDs);

    }

    /**
     * @param ae
     * @throws IOException
     */
    public void onNewGame(ActionEvent ae) throws IOException {

        System.out.println("fake new game was pressed");

        URL xmlResource = getClass().getResource("/mapGoBack2.fxml");
        // URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapGoBackController.class, new MapGoBackController(client));
        // controllers.put(MapChooseActionController.class, new
        // MapChooseActionController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));

        App.getPrimaryStage().setScene(scene);

    }

    public void onLogOut() throws IOException {

        client.logOut();
        App.loadScenefromMain("login-page");
    }

    /**
     * @throws IOException
     * @throws ClassNotFoundException
     *                                goes to waiting screen after starting one game
     */
    public void goToWaitingScreen() throws IOException, ClassNotFoundException {
        URL xmlResource = getClass().getResource("/waiting-room.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        HashMap<Class<?>, Object> controllers = new HashMap<>();
        // controllers.put(GoBackController.class, new GoBackController());
        controllers.put(JoinableGamesController.class, new JoinableGamesController(client));
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("waitin-room", scene);

        String color = client.receiveColor();
        multipleGamesController.setName(color);

        client.setColor(color);
        // App.getPrimaryStage().setScene(scene);
        App.getPrimaryStage().setScene(scene);
        Integer gameID = client.getGameID();

        Platform.runLater(() -> {
            // code to be executed after the scene is set
            try {
                // client.getGame();
                Game game = client.getGame();
                // client.getGameConfirmation();

                game.setGameID(gameID);
                client.setGameID(gameID);
                System.out.println("from the one that creates the game: " + client.getCurrentGameID());
                goToPlacement(game);

            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

    }

    /**
     * @param game
     * @throws IOException
     * @throws ClassNotFoundException
     *                                loads the necessary controllers
     */
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
        App.addScenetoMain("placement", scene);

        App.getPrimaryStage().setScene(scene);
        // Game game = client.getGame();
        // System.out.println("we received a game!");
    }

    /**
     * @param gameIDs
     * @throws IOException
     * @throws ClassNotFoundException
     *                                shows the joinable games in the joining games
     *                                page
     */
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

    /**
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void showGamesStarted() throws ClassNotFoundException, IOException {

        ArrayList<Integer> gameIDs = client.getGamesStarted();
        System.out.println(gameIDs.size());
        for (int i = 0; i < gameIDs.size(); i++) {

            gameButtons[i].setVisible(true);

            // System.out.println("the size of the gameID array is:" + gameIDs.size());

            gameButtons[i].setText(gameIDs.get(i).toString());

        }
    }

    /**
     * @param updatedGame
     * @throws IOException
     *                     goes to placing actions
     */
    public void goToPlacingActionsScreen(Game updatedGame) throws IOException {

        URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        // System.out.println("just before going to placing actions" +
        // client.getCurrentGameID());
        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        controllers.put(MapChooseActionController.class, new MapChooseActionController(client, updatedGame));
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("submit-actions", scene);

        App.getPrimaryStage().setScene(scene);
    }
}
