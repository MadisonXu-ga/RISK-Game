package edu.duke.ece651.team5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class TerritoryController {

    @FXML
    AnchorPane childRoot;

    public Button[] buttons;

    @FXML
    public void initialize() {
        buttons = childRoot.lookupAll(".button").toArray(new Button[0]);
        System.out.println(buttons.length);
    }

    // public void

}
