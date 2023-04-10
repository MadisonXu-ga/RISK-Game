package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.util.ArrayList;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class JoinableGamesController {

    public Client client;
    @FXML
    AnchorPane multipleGameButtons;
    private Button[] gameButtons;

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

    public void onsaveAndExit(ActionEvent ae) throws ClassNotFoundException, IOException {

        // TODO create the disconnect action with server

        App.loadScenefromMain("multiple-games");
    }

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
                multipleGamesController.refresh();
            }
            System.out.println(msg);

        }
    }

}
