package edu.duke.ece651.team5.client.controller;

import java.util.ArrayList;

import edu.duke.ece651.team5.client.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class JoinableGamesController {

    @FXML
    AnchorPane multipleGameButtons;
    private Button[] gameButtons;

    @FXML
    public void initialize() {
        gameButtons = multipleGameButtons.lookupAll(".button").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {
            // System.out.println(gameButtons[i].getId());
            // Set the visibility of the button based on the corresponding boolean value
            if (gameButtons[i].getId().contains("Game")) {
                gameButtons[i].setVisible(false);
            }
        }
    }

    public void showJoinableGames(ArrayList<Integer> gameIDs) {

        System.out.println(gameButtons.length);
        for (int i = 0; i < gameIDs.size(); i++) {

            gameButtons[i].setVisible(true);
            gameButtons[i].setText(gameIDs.get(i).toString());
        }
    }

    public void onsaveAndExit(ActionEvent ae) {

        // TODO create the disconnect action with server
        App.loadScenefromMain("multiple-games");
    }

}
