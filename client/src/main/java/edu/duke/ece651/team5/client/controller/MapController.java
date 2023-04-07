package edu.duke.ece651.team5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MapController {

    @FXML
    AnchorPane rightSideScreen;

    public Button[] gameButtons;

    @FXML
    public void initialize() {
        gameButtons = rightSideScreen.lookupAll(".button:@territory.css").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {

            // Set the visibility of the button based on the corresponding boolean value
            // gameButtons[i].setStyle("-fx-background-color: rgba(0, 0, 255);");
            if (i < 10) {
                gameButtons[i].getStyleClass().add("redPlayer");
            } else {
                gameButtons[i].getStyleClass().add("bluePlayer");
            }

            // makeButtonDarker(gameButtons[i]);
            System.out.println(i);
        }

    }

}
