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
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MapChooseActionController extends MapController {

    // Game game;

    @FXML
    Text playerNametxt;

    @FXML
    Text gameID;

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
    }

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

    public void onDone() throws ClassNotFoundException, IOException {

        Action emptyAction = new Action(new ArrayList<>(), new ArrayList<>(), null, new ArrayList<>());

        System.out.println("Current game ID before sending the orders: " + client.getCurrentGameID());
        String ActionResults = client.sendOrder(client.getCurrentGameID(), emptyAction);
        System.out.println("From server: " + ActionResults);
    }

}
