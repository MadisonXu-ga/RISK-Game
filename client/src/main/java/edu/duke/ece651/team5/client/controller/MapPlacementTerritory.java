package edu.duke.ece651.team5.client.controller;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MapPlacementTerritory extends MapController {

    public MapPlacementTerritory(Client client) {
        super(client);

    }

    public Text[] gameButtons;

    @FXML
    public void initialize() {
        super.initialize();

        for (Node node : rightSideScreen.getChildren()) {
            if (node instanceof Text) {
                Text territoryText = (Text) node;
                if (territoryText.getText().toLowerCase().contains("territory")) {
                    // Do something with the button
                    System.out.println("Button with 'territory' in its text: " + territoryText.getText());
                }
            }
        }
    }

    public void onsubmitPlacement() {

    }

}
