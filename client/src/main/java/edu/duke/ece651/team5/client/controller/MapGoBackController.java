package edu.duke.ece651.team5.client.controller;

import java.time.LocalTime;
import java.util.ArrayList;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Popup;

public class MapGoBackController extends MapController {

    public MapGoBackController(Client client) {
        super(client);
    }

    @FXML
    Button goBackbtn;

    @FXML
    ComboBox<SoldierLevel> unitsComboBox;

    @FXML
    ComboBox<String> sourceTerritorybtn;

    @FXML
    ComboBox<String> destTerritorybtn;

    @FXML
    public void initialize() {
        super.initialize();
        ObservableList<SoldierLevel> options = FXCollections.observableArrayList(SoldierLevel.values());
        unitsComboBox.setItems(options);
        onSourceTerritorySelection();
        onDestTerritorySelection();

    }

    public void onUnitSelection() {

        SoldierLevel selectedLevel = unitsComboBox.getValue();
        System.out.println("Selected Soldier Level: " + selectedLevel);

    }

    public void onSourceTerritorySelection() {

        ObservableList<String> sourceOptions = FXCollections.observableArrayList(list);
        sourceTerritorybtn.setItems(sourceOptions);
        // ObservableList<String> destOptions = FXCollections.observableArrayList(list);
        // destTerritorybtn.setItems(destOptions);

    }

    public void onDestTerritorySelection() {

        ObservableList<String> destOptions = FXCollections.observableArrayList(list);
        destTerritorybtn.setItems(destOptions);

    }

    public void onSubmitMove() {

        String sourceTerritory = sourceTerritorybtn.getValue();
        sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
        String destTerritorry = destTerritorybtn.getValue();
        destTerritorybtn.setValue(destTerritorybtn.getId());
        System.out.println("you are trying to move from: " + sourceTerritory + " to " + destTerritorry);
    }

}
