package edu.duke.ece651.team5.client.controller;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MapController extends GoBackController {

    @FXML
    AnchorPane rightSideScreen;

    public Button[] gameButtons;
    public HashMap<String, Button> buttonsHashmap;

    @FXML
    public void initialize() {
        gameButtons = rightSideScreen.lookupAll(".button:@territory.css").toArray(new Button[0]);

        for (int i = 0; i < gameButtons.length; i++) {

            // System.out.println(gameButtons[i]);
            if (!gameButtons[i].getId().contains("btn")) {

                System.out.println("territory " + i + ": " + gameButtons[i].getId());

            }
            // Set the visibility of the button based on the corresponding boolean value
            // gameButtons[i].setStyle("-fx-background-color: rgba(0, 0, 255);");
            if (i < 10) {
                assignButtonToPlayer(gameButtons[i], "red");
            } else if (i < 17) {
                gameButtons[i].setDisable(true);
            }

            else {
                assignButtonToPlayer(gameButtons[i], "blue");
            }

            // makeButtonDarker(gameButtons[i]);
            // System.out.println(i);
        }

    }

    /**
     * @param button to be disabled (and not clickable). It will be shown as grey
     */
    public void disableButton(Button button) {
        button.setDisable(true);

    }

    /**
     * you can call this function to show the territory the same color
     * as the player that owns it
     * 
     * @param button
     */
    public void assignButtonToPlayer(Button button, String color) {

        String getStyle = color + "Playerbtn";
        button.getStyleClass().add(getStyle);
    }

}
