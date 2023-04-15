package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.utils.ResourceConsumeCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
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
    ComboBox<SoldierLevel> unitsComboBoxTarget;

    @FXML
    ComboBox<String> sourceTerritorybtn;

    @FXML
    ComboBox<String> destTerritorybtn;

    @FXML
    Spinner<Integer> numberUnitsSpinner;

    @FXML
    Text actionMessage;

    @FXML
    Text techAmnt;

    @FXML
    Text foodAmnt;

    @FXML
    public void initialize() {
        super.initialize();
        ObservableList<SoldierLevel> options = FXCollections.observableArrayList(SoldierLevel.values());
        unitsComboBox.setItems(options);
        unitsComboBoxTarget.setItems(options);
        // onSourceTerritorySelection();
        // onDestTerritorySelection();
        showTerritoryColors(showAll);
        gameID.setText("gameID: " + client.getCurrentGameID().toString());
        playerNametxt.setText("Name: " + client.getColor());
        Integer techAmntInt = game.getPlayerByName(client.getColor())
                .getResourceCount(new Resource(ResourceType.TECHNOLOGY));
        techAmnt.setText(techAmntInt.toString());
        Integer foodAmntInt = game.getPlayerByName(client.getColor())
                .getResourceCount(new Resource(ResourceType.FOOD));
        foodAmnt.setText(foodAmntInt.toString());

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

        // int numberUnits = numberUnitsSpinner.getValue();
        SoldierArmy soldierArmy = new SoldierArmy();
        // soldierArmy.addSoldier(new Soldier(unitsComboBox.getValue()), numberUnits);

        try {
            // get the selected value from the ComboBox
            int numberUnits = numberUnitsSpinner.getValue();
            soldierArmy.addSoldier(new Soldier(unitsComboBox.getValue()), numberUnits);

            // process the selected value and value from spinner...
        } catch (NullPointerException | NumberFormatException e) {
            // handle invalid input
            e.printStackTrace();
        }
        Player player = game.getPlayerByName(client.getColor());

        if (game.getMap().getTerritoryByName(sourceTerritory) != null
                && game.getMap().getTerritoryByName(destTerritorry) != null
                && !(soldierArmy.equals(new SoldierArmy()))) {
            // code to execute if all variables are not null
            MoveOrder moveOrder = new MoveOrder(sourceTerritory, destTerritorry, soldierArmy, player);

            Integer moneyCost = ResourceConsumeCalculator.computeFoodConsumeForMove(moveOrder, game.getMap());
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to submit? It will cost you " + moneyCost + " food points");
            alert.setContentText("Press OK to submit, or Cancel to cancel.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mapChooseActionController.foodSpent += moneyCost;
                mapChooseActionController.moveOrders.add(moveOrder);
                mapChooseActionController.foodSpentTurn
                        .setText(Integer.toString(mapChooseActionController.foodSpent));
                actionMessage.setText("");
                actionMessage.setText("");
            }

            // mapChooseActionController.moveOrders.add(moveOrder);
            // actionMessage.setText("");
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

        SoldierArmy soldierArmy = new SoldierArmy();

        try {
            // get the selected value from the ComboBox
            int numberUnits = numberUnitsSpinner.getValue();
            soldierArmy.addSoldier(new Soldier(unitsComboBox.getValue()), numberUnits);

            // process the selected value and value from spinner...
        } catch (NullPointerException | NumberFormatException e) {
            // handle invalid input
            e.printStackTrace();
        }

        Player player = game.getPlayerByName(client.getColor());

        if (game.getMap().getTerritoryByName(sourceTerritory) != null
                && game.getMap().getTerritoryByName(destTerritorry) != null
                && !(soldierArmy.equals(new SoldierArmy()))) {
            // code to execute if all variables are not null
            AttackOrder attackOrder = new AttackOrder(sourceTerritory, destTerritorry, soldierArmy, player);

            Integer moneyCost = ResourceConsumeCalculator.computeFoodConsumeForAttack(attackOrder, game.getMap());
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to submit? It will cost you " + moneyCost + " food points");
            alert.setContentText("Press OK to submit, or Cancel to cancel.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mapChooseActionController.foodSpent += moneyCost;
                mapChooseActionController.attackOrders.add(attackOrder);

                mapChooseActionController.foodSpentTurn
                        .setText(Integer.toString(mapChooseActionController.foodSpent));
                actionMessage.setText("");

                actionMessage.setText("");
            }

            // mapChooseActionController.attackOrders.add(attackOrder);
            // actionMessage.setText("");
        } else {
            sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
            destTerritorybtn.setValue(destTerritorybtn.getId());
            unitsComboBox.setValue(null);
            numberUnitsSpinner.getValueFactory().setValue(0);
            actionMessage.setText("Invalid action move. Try again!");

        }

    }

    public void onSubmitUpgrade() {

        if (mapChooseActionController.upgradeOrders == null) {
            mapChooseActionController.upgradeOrders = new ArrayList<>();
        }
        String sourceTerritory = sourceTerritorybtn.getValue();
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();

        try {
            // get the selected value from the ComboBox

            soldierToUpgrade.put(new Pair<>(new Soldier(unitsComboBox.getValue()), numberUnitsSpinner.getValue()),
                    unitsComboBoxTarget.getValue());

            // System.out.println("parameters for constructor:");
            // System.out.println(numberUnitsSpinner.getValue());
            // System.out.println(unitsComboBox.getValue());
            // System.out.println(sourceTerritory);
            // System.out.println(unitsComboBoxTarget.getValue());

            // process the selected value and value from spinner...
        } catch (NullPointerException | NumberFormatException e) {
            // handle invalid input
            System.out.println("exception");
            e.printStackTrace();
        }

        if (game.getMap().getTerritoryByName(sourceTerritory) != null
                && !(soldierToUpgrade.equals(new HashMap<>()))) {

            UpgradeOrder upgradeOrder = new UpgradeOrder(sourceTerritory, soldierToUpgrade,
                    game.getPlayerByName(client.getColor()));

            Integer moneyCost = ResourceConsumeCalculator.computeTechConsumeForUpgrade(upgradeOrder);
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to submit? It will cost you " + moneyCost + " food points");
            alert.setContentText("Press OK to submit, or Cancel to cancel.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mapChooseActionController.moneySpent += moneyCost;
                mapChooseActionController.upgradeOrders.add(upgradeOrder);

                mapChooseActionController.moenySpentTurn
                        .setText(Integer.toString(mapChooseActionController.moneySpent));
                actionMessage.setText("");
            }
            // System.out.println("just before adding it to array: " +
            // mapChooseActionController.upgradeOrders.size());

            // mapChooseActionController.upgradeOrders.add(upgradeOrder);

            // System.out.println("just after adding it to array: " +
            // mapChooseActionController.upgradeOrders.size());
        } else {
            sourceTerritorybtn.setValue(sourceTerritorybtn.getId());
            System.out.println("Value: " + soldierToUpgrade);

            unitsComboBox.setValue(null);
            unitsComboBoxTarget.setValue(null);
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
                matchingButton.getStyleClass().clear();
                matchingButton.getStyleClass().add(sourceTerri + "btn");
                matchingButton.getStyleClass()
                        .add(game.getMap().getTerritoryByName(sourceTerri).getOwner().getName() + "Playerbtn");
                // matchingButton.setStyle("-fx-background-color: red;");
            }
        }

        for (String destTerritory : destList) {

            Button matchingButton = getMatchingGameButton(destTerritory);
            if (matchingButton != null) {
                // System.out.println("color: [" + client.getColor() + "]");
                // matchingButton.getStyleClass()
                // .add(game.getMap().getTerritoryByName(destTerritory).getOwner().getName() +
                // "Playerbtn");
                matchingButton.getStyleClass().clear();
                matchingButton.getStyleClass().add(destTerritory + "btn");
                matchingButton.getStyleClass()
                        .add(game.getMap().getTerritoryByName(destTerritory).getOwner().getName() + "Playerbtn");
                // matchingButton.setStyle("-fx-background-color: red;");
            }
        }
        // App.loadScenefromMain("submit-actions");

    }

    public void onBackbtn() {

        MapChooseActionController actionController = (MapChooseActionController) App.loadController("submit-actions");
        actionController.initialize();
        App.loadScenefromMain("submit-actions");
    }

}
