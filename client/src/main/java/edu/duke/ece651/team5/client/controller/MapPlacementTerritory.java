package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.constant.Constants;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MapPlacementTerritory extends MapController {

    private Integer availableUnits;
    @FXML
    Text availableUnitTxt;
    @FXML
    Text unitsPlacedText;

    @FXML
    Text playerNametxt;

    public MapPlacementTerritory(Client client, Game game) {
        super(client, game);

    }

    public ArrayList<Text> territoryNamesText;
    public ArrayList<Spinner<Integer>> territoryFields;

    @FXML
    public void initialize() {
        super.initialize();

        territoryNamesText = new ArrayList<>();
        territoryFields = new ArrayList<>();
        ownedTerritories = new ArrayList<>();
        availableUnits = Constants.AVAILABLE_UNIT;
        // System.out.println("Avaialable: " + availableUnits);
        availableUnitTxt.setText(availableUnits.toString());
        playerNametxt.setText("Name: " + client.getColor());

        colorTerritoriesYouOwn();
        int terrIdx = 0;
        for (Node node : rightSideScreen.getChildren()) {

            if (node instanceof Spinner) {

                Spinner<Integer> textField = (Spinner<Integer>) node;
                Integer amountTerritories = game.getPlayerByName(client.getColor()).getTerritories().size();
                SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
                        availableUnits - amountTerritories + 1,
                        0);
                textField.setValueFactory(valueFactory);
                if (terrIdx <= ownedTerritories.size()) {
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
                        territoryText.setText(ownedTerritories.get(terrIdx).getName());
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

        // System.out
        // .println("textFields size: " + territoryFields.size() + ", textp[] size: " +
        // territoryNamesText.size());
    }

    public void onsubmitPlacement() throws IOException, ClassNotFoundException {

        HashMap<String, Integer> placementOrders = new HashMap<>();

        Integer sum = addFields();
        for (int i = 0; i < territoryFields.size(); i++) {

            String territoString = territoryNamesText.get(i).getText();
            Integer unitsAmount = territoryFields.get(i).getValue();

            if (unitsAmount > 0) {
                placementOrders.put(territoString, unitsAmount);
            }
        }
        if (sum != 50) {
            // System.out.println("The sum is: " + sum);
            unitsPlacedText.setText("the amount of units adds up to " + sum.toString() + ". Use 50");
        }

        if (sum == 50) {

            // client.sendPlacementPrompt();
            String placementResult = client.sendPlacementOrder(game.getGameID(), placementOrders);
            Game updatedGame = client.recvUpdatedGame();
            // System.out.println(placementResult);

            unitsPlacedText.setText("");
            resetFields();
            goToPlacingActionsScreen(updatedGame);

        }

        if (territoryFields.size() == placementOrders.size()) {
            System.out.println(placementOrders);
            System.out.println(placementOrders.size());
        }
    }

    public void resetFields() {

        for (Spinner<Integer> field : territoryFields) {
            field.getValueFactory().setValue(0);

        }
    }

    public Integer addFields() {
        Integer sum = 0;
        for (Spinner<Integer> field : territoryFields) {

            sum += field.getValue();
        }

        return sum;

    }

    // public void getPlacementOrder(){
    // HashMap<String, Integer> placementOrder = new HashMap<>();

    // for (int i =0; i < territoryFields.size(); i++){

    // place
    // }

    // }

    public void goToPlacingActionsScreen(Game updatedGame) throws IOException {

        URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        // System.out.println("just before going to placing actions" +
        // client.getCurrentGameID());
        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        MapChooseActionController mapChooseActionController = new MapChooseActionController(client, updatedGame);
        controllers.put(MapChooseActionController.class, mapChooseActionController);
        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addControllertoMain("submit-actions", mapChooseActionController);
        App.addScenetoMain("submit-actions", scene);

        App.getPrimaryStage().setScene(scene);
    }
}
