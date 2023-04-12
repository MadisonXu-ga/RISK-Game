package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
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

public class MapChooseActionController extends MapController {

    // Game game;

    public MapChooseActionController(Client client, Game game) {
        super(client, game);
        // this.game = game;
    }

    ArrayList<AttackOrder> attackOrders;
    ArrayList<MoveOrder> moveOrders;
    ResearchOrder researchOrder;
    ArrayList<UpgradeOrder> upgradeOrders;

    @FXML
    ComboBox<SoldierLevel> unitsComboBox;

    @FXML
    public void initialize() {
        super.initialize();
        // colorTerritoriesbyOwner();
        showTerritoryColors(true);
    }

    public void onMoveAction() throws IOException {

        URL xmlResource = getClass().getResource("/mapGoBack2.fxml");
        // URL xmlResource = getClass().getResource("/mapSubmitActions.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(MapGoBackController.class, new MapGoBackController(client, game, false));
        // controllers.put(MapChooseActionController.class, new
        // MapChooseActionController());
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));

        App.getPrimaryStage().setScene(scene);
    }
}
