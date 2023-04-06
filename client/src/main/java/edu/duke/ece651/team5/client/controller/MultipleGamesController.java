package edu.duke.ece651.team5.client.controller;

import java.io.IOException;

import edu.duke.ece651.team5.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MultipleGamesController {

    @FXML
    AnchorPane multipleGameButtons;

    @FXML
    Button beginNewGame;
    @FXML
    Button joinOtherGames;

    public Button[] gameButtons;
    public Client client;

    @FXML
    public void initialize() {
        gameButtons = multipleGameButtons.lookupAll(".button").toArray(new Button[0]);
        System.out.println(gameButtons.length);
    }

    public MultipleGamesController(Client client) {

        this.client = client;

    }

    public void onGame1(ActionEvent ae) {

        System.out.print(gameButtons.length);

    }

    public void onBeginNewGame(ActionEvent ae) throws IOException {
        client.beginNewGame();
    }

}
