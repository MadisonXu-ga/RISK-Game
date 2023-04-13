package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.utils.ResourceConsumeCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MapChooseActionController extends MapController {

    // Game game;

    @FXML
    Text playerNametxt;

    @FXML
    Text gameID;

    @FXML
    Text foodAmnt;

    @FXML
    Text techAmnt;

    public ResourceConsumeCalculator calculator;

    /**
     * @param client
     * @param game
     */
    public MapChooseActionController(Client client, Game game) {
        super(client, game);
        // this.game = game;
    }

    public ArrayList<AttackOrder> attackOrders;
    public ArrayList<MoveOrder> moveOrders;
    public ResearchOrder researchOrder;
    public ArrayList<UpgradeOrder> upgradeOrders;

    @FXML
    ComboBox<SoldierLevel> unitsComboBox;

    @FXML
    public void initialize() {
        super.initialize();
        // colorTerritoriesbyOwner();
        showTerritoryColors(true);
        gameID.setText("gameID: " + client.getCurrentGameID().toString());
        playerNametxt.setText("Name: " + client.getColor());
        attackOrders = new ArrayList<>();
        moveOrders = new ArrayList<>();
        upgradeOrders = new ArrayList<>();
        this.calculator = new ResourceConsumeCalculator();
        Integer foodInt = game.getPlayerByName(client.getColor()).getResourceCount(new Resource(ResourceType.FOOD));
        foodAmnt.setText(foodInt.toString());
        Integer techAmntInt = game.getPlayerByName(client.getColor())
                .getResourceCount(new Resource(ResourceType.TECHNOLOGY));
        techAmnt.setText(techAmntInt.toString());

    }

    /**
     * @throws IOException
     */
    public void onMoveAction() throws IOException {

        URL xmlResource = getClass().getResource("/mapGoBack2.fxml");
        // URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        MapGoBackController mapGoBackController = new MapGoBackController(client, game, false, this);
        controllers.put(MapGoBackController.class, mapGoBackController);
        // controllers.put(MapChooseActionController.class, new
        // MapChooseActionController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));

        List<Territory> ownTerritory = game.getPlayerByName(client.getColor()).getTerritories();
        ArrayList<String> ownTerritoriesStr = mapGoBackController.setList(ownTerritory);

        // App.getPrimaryStage().setScene(scene);
        mapGoBackController.refresh(ownTerritoriesStr, ownTerritoriesStr, client.getColor(), client.getColor());
        System.out.println(ownTerritoriesStr);
        App.getPrimaryStage().setScene(scene);
    }

    /**
     * attack button for submitting orders
     * 
     * @throws IOException
     */
    public void onAttackAction() throws IOException {

        URL xmlResource = getClass().getResource("/attack-map.fxml");
        // URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        MapGoBackController mapGoBackController = new MapGoBackController(client, game, false, this);
        controllers.put(MapGoBackController.class, mapGoBackController);
        // controllers.put(MapChooseActionController.class, new
        // MapChooseActionController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));

        List<Territory> ownTerritory = game.getPlayerByName(client.getColor()).getTerritories();

        List<Territory> enemyTerritory = new ArrayList<>();

        for (Territory territory : ownTerritory) {

            enemyTerritory.addAll(game.getMap().getNeighbors(territory.getId(), false,
                    game.getPlayerByName(client.getColor())));

        }
        ArrayList<String> ownTerritoriesStr = mapGoBackController.setList(ownTerritory);
        ArrayList<String> enemyTerritoriesStr = mapGoBackController.setList(enemyTerritory);

        // App.getPrimaryStage().setScene(scene);
        mapGoBackController.refresh(ownTerritoriesStr, enemyTerritoriesStr, client.getColor(), client.getColor());
        System.out.println(ownTerritoriesStr);
        App.getPrimaryStage().setScene(scene);
    }

    public void onResearchAction() {

        ResearchOrder resOrder = new ResearchOrder(game.getPlayerByName(client.getColor()), game);
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to upgrade? It will cost you "
                        + ResourceConsumeCalculator.computeTechConsumeForResearch(resOrder) + " tech resources");
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);

        // Show the confirmation dialog and wait for the user's response
        ButtonType confirmButton = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // Check if the user clicked the "OK" button
        if (confirmButton == ButtonType.OK) {
            researchOrder = new ResearchOrder(game.getPlayerByName(client.getColor()), game);
        } else {
            // User clicked Cancel, do nothing or handle accordingly
            // ...
        }
    }

    public void onDone() throws ClassNotFoundException, IOException {

        Action emptyAction = new Action(attackOrders, moveOrders, null, new ArrayList<>());

        System.out.println("Current game ID before sending the orders: " + client.getCurrentGameID());
        String ActionResults = client.sendOrder(client.getCurrentGameID(), emptyAction);

        if (!ActionResults.equals("Order succeeded")) {
            attackOrders = new ArrayList<>();
            moveOrders = new ArrayList<>();
            upgradeOrders = new ArrayList<>();
            researchOrder = null;
        }

        else {
            game = client.updatedGameAfterTurn();
            client.checkWin();
            client.checkLost();
            initialize();
            App.loadScenefromMain("submit-actions");
            attackOrders = new ArrayList<>();
            moveOrders = new ArrayList<>();
            upgradeOrders = new ArrayList<>();
            researchOrder = null;
        }

    }

}
