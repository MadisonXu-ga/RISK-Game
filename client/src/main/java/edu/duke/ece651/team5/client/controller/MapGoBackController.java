package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class MapGoBackController extends MapController {

    public boolean showAll;
    public MapChooseActionController mapChooseActionController;

    public MapGoBackController(Client client) {
        super(client);
        this.showAll = true;
    }

    public MapGoBackController(Client client, Game game) {
        super(client, game);
        this.showAll = true;
    }

    public MapGoBackController(Client client, Game game, boolean showAll) {
        super(client, game);
        this.showAll = showAll;
    }

    public MapGoBackController(Client client, Game game, boolean showAll,
            MapChooseActionController mapChooseActionController) {
        super(client, game);
        this.showAll = showAll;
        this.mapChooseActionController = mapChooseActionController;
    }

    @FXML
    Button goBackbtn;

    @FXML
    Text playerNametxt;

    @FXML
    Text gameID;

    @FXML
    ComboBox<SoldierLevel> unitsComboBox;

    @FXML
    ComboBox<String> sourceTerritorybtn;

    @FXML
    ComboBox<String> destTerritorybtn;

    @FXML
    Spinner<Integer> numberUnitsSpinner;

    @FXML
    Text actionMessage;

    @FXML
    public void initialize() {
        super.initialize();
        ObservableList<SoldierLevel> options = FXCollections.observableArrayList(SoldierLevel.values());
        unitsComboBox.setItems(options);
        // onSourceTerritorySelection();
        // onDestTerritorySelection();
        showTerritoryColors(showAll);
        gameID.setText("gameID: " + client.getCurrentGameID().toString());
        playerNametxt.setText("Name: " + client.getColor());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
                100,
                0);
        numberUnitsSpinner.setValueFactory(valueFactory);

    }

    public void onUnitSelection() {

        SoldierLevel selectedLevel = unitsComboBox.getValue();
        System.out.println("Selected Soldier Level: " + selectedLevel);

    }

    // public void onSourceTerritorySelection() {

    // ObservableList<String> sourceOptions =
    // FXCollections.observableArrayList(list);
    // sourceTerritorybtn.setItems(sourceOptions);
    // // ObservableList<String> destOptions =
    // FXCollections.observableArrayList(list);
    // // destTerritorybtn.setItems(destOptions);

    // }

    // public void onDestTerritorySelection() {

    // ObservableList<String> destOptions = FXCollections.observableArrayList(list);
    // destTerritorybtn.setItems(destOptions);

    // }

    public void onSubmitMove() {

        String sourceTerritory = sourceTerritorybtn.getValue();
        sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
        String destTerritorry = destTerritorybtn.getValue();
        destTerritorybtn.setValue(destTerritorybtn.getId());
        System.out.println("you are trying to move from: " + sourceTerritory + " to " + destTerritorry);

        int numberUnits = numberUnitsSpinner.getValue();
        SoldierArmy soldierArmy = new SoldierArmy();
        soldierArmy.addSoldier(new Soldier(unitsComboBox.getValue()), numberUnits);
        Player player = game.getPlayerByName(client.getColor());

        if (sourceTerritory != sourceTerritorybtn.getId() && destTerritorry != destTerritorybtn.getValue()
                && soldierArmy != null) {
            // code to execute if all variables are not null
            MoveOrder moveOrder = new MoveOrder(sourceTerritory, destTerritorry, soldierArmy, player);
            mapChooseActionController.moveOrders.add(moveOrder);
            actionMessage.setText("");
        } else {
            sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
            destTerritorybtn.setValue(destTerritorybtn.getId());
            unitsComboBox.setValue(null);
            numberUnitsSpinner.getValueFactory().setValue(0);
            actionMessage.setText("Invalid action move. Try again!");

        }

        // System.out.println("before - Move order object size is " +
        // mapChooseActionController.moveOrders.size());

        // System.out.println("after - Move order object size is " +
        // mapChooseActionController.moveOrders.size());
    }

    public void onSubmitAttack() {

        String sourceTerritory = sourceTerritorybtn.getValue();
        sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
        String destTerritorry = destTerritorybtn.getValue();
        destTerritorybtn.setValue(destTerritorybtn.getId());
        System.out.println("you are trying to move from: " + sourceTerritory + " to " + destTerritorry);

        int numberUnits = numberUnitsSpinner.getValue();
        SoldierArmy soldierArmy = new SoldierArmy();
        soldierArmy.addSoldier(new Soldier(unitsComboBox.getValue()), numberUnits);
        Player player = game.getPlayerByName(client.getColor());

        if (sourceTerritory != sourceTerritorybtn.getId() && destTerritorry != destTerritorybtn.getValue()
                && soldierArmy != null) {
            // code to execute if all variables are not null
            MoveOrder moveOrder = new MoveOrder(sourceTerritory, destTerritorry, soldierArmy, player);
            mapChooseActionController.moveOrders.add(moveOrder);
            actionMessage.setText("");
        } else {
            sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
            destTerritorybtn.setValue(destTerritorybtn.getId());
            unitsComboBox.setValue(null);
            numberUnitsSpinner.getValueFactory().setValue(0);
            actionMessage.setText("Invalid action move. Try again!");

        }

    }

    public ArrayList<String> setList(List<Territory> territories) {

        ArrayList<String> listToReturn = new ArrayList<>();

        for (Territory territory : territories) {
            listToReturn.add(territory.getName());
        }

        return listToReturn;

    }

    public void refresh(ArrayList<String> sourceList, ArrayList<String> destList, String colorSource,
            String colorDest) {

        ObservableList<String> sourceOptions = FXCollections.observableArrayList(sourceList);
        sourceTerritorybtn.getItems().clear();
        sourceTerritorybtn.setItems(sourceOptions);

        ObservableList<String> destOptions = FXCollections.observableArrayList(destList);
        destTerritorybtn.getItems().clear();
        destTerritorybtn.setItems(destOptions);

        for (String sourceTerri : sourceList) {

            Button matchingButton = getMatchingGameButton(sourceTerri);
            System.out.println("Territory button: " + matchingButton.getId());
            if (matchingButton != null) {
                // System.out.println("color: [" + client.getColor() + "]");
                matchingButton.setStyle("-fx-background-color: " + colorSource + ";");
                // matchingButton.setStyle("-fx-background-color: red;");
            }
        }

        for (String destTerritory : destList) {

            Button matchingButton = getMatchingGameButton(destTerritory);
            if (matchingButton != null) {
                // System.out.println("color: [" + client.getColor() + "]");
                matchingButton.setStyle("-fx-background-color: " + colorDest + ";");
                // matchingButton.setStyle("-fx-background-color: red;");
            }
        }
        // App.loadScenefromMain("submit-actions");

    }

    public void onBackbtn() {
        App.loadScenefromMain("submit-actions");
    }

}
