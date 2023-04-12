package edu.duke.ece651.team5.client.controller;

import java.util.ArrayList;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

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
        colorTerritoriesbyOwner();
    }

    // public void onSubmitMove() {

    // String selectedItem = sourceTerritorybtn.getValue();
    // System.out.println("Selected item: " + selectedItem);
    // }
}
