package edu.duke.ece651.team5.client.controller;

import java.io.IOException;

import edu.duke.ece651.team5.client.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GoBackController {

    public void onSaveAndExit(ActionEvent ae) throws IOException, ClassNotFoundException {

        // TODO create the disconnect action with server
        // JoinableGamesController joinableGamesController = (JoinableGamesController)
        // App.loadController("waiting-room");
        // joinableGamesController.initialize();

        MultipleGamesController multipleGamesController = (MultipleGamesController) App
                .loadController("multiple-games");
        multipleGamesController.refresh();
        System.out.println("this is printing from the gobackcontroller");
        App.loadScenefromMain("multiple-games");
    }
}
