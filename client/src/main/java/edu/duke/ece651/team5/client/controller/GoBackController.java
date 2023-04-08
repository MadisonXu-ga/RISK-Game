package edu.duke.ece651.team5.client.controller;

import edu.duke.ece651.team5.client.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GoBackController {

    public void onSaveAndExit(ActionEvent ae) {

        // TODO create the disconnect action with server
        App.loadScenefromMain("multiple-games");
    }
}
