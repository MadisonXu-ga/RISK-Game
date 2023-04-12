package edu.duke.ece651.team5.client.controller;

import java.util.ArrayList;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MapPlacementTerritory extends MapController {

    private Game game;

    public MapPlacementTerritory(Client client, Game game) {
        super(client);
        this.game = game;

    }

    public ArrayList<Text> territoryNamesText;
    public ArrayList<TextField> territoryFields;
    public ArrayList<Territory> ownedTerritories;

    @FXML
    public void initialize() {
        super.initialize();
        territoryNamesText = new ArrayList<>();
        territoryFields = new ArrayList<>();
        ownedTerritories = new ArrayList<>();

        for (Territory territory : game.getPlayerByName(client.getColor()).getTerritories()) {
            ownedTerritories.add(territory);
            Button matchingButton = getMatchingGameButton(territory.getName());
            if (matchingButton != null) {
                // System.out.println("color: [" + client.getColor() + "]");
                assignButtonToPlayer(matchingButton, client.getColor());
                // matchingButton.setStyle("-fx-background-color: red;");
            }
        }
        int terrIdx = 0;
        for (Node node : rightSideScreen.getChildren()) {

            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                if (terrIdx < ownedTerritories.size()) {
                    territoryFields.add(textField);
                }
                if (terrIdx > ownedTerritories.size()) {
                    textField.setVisible(false);
                }

            }
            if (node instanceof Text) {
                Text territoryText = (Text) node;
                if (territoryText.getText().toLowerCase().contains("territory")) {

                    // territoryText.setVisible(false);
                    if (terrIdx < ownedTerritories.size()) {
                        territoryNamesText.add(territoryText);
                        territoryText.setText(ownedTerritories.get(terrIdx).getName() + ":");
                    }
                    // System.out.println(terrIdx + "size: " + ownedTerritories.size());

                    terrIdx += 1;

                    if (terrIdx > ownedTerritories.size()) {
                        territoryText.setVisible(false);
                    }
                    // Do something with the button
                    // System.out.println("Button with 'territory' in its text: " +
                    // territoryText.getText());
                }
            }
        }

        System.out
                .println("textFields size: " + territoryFields.size() + ", textp[] size: " + territoryNamesText.size());
    }

    public void onsubmitPlacement() {

        for (TextField text : territoryFields) {

            text.setText("");
        }

    }

}
