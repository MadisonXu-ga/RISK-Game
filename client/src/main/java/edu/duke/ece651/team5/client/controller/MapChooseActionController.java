package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AllianceOrder;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.resource.WeatherType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.utils.ResourceConsumeCalculator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MapChooseActionController extends MapController {

    // Game game;

    @FXML
    Text playerNametxt;

    @FXML
    Text gameID;

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

    ResourceConsumeCalculator calculator;

    @FXML
    Text techAmnt;

    @FXML
    Text foodAmnt;

    @FXML
    Text turnFeedback;

    @FXML
    Text currTechnologyLevel;

    @FXML
    Button researchbtn;

    @FXML
    Text moenySpentTurn;

    @FXML
    Text foodSpentTurn;

    @FXML
    Text allianceMessageText;

    @FXML
    ComboBox<String> playersCombobox;

    @FXML
    Button formAlliance;

    @FXML
    Text WeatherText;

    @FXML
    Text EventText;

    public Integer moneySpent;
    public Integer foodSpent;
    private ListView<String> messageListView;
    private Stage secondStage;
    // private ObservableList<String> messages =
    // FXCollections.observableArrayList();

    /*
     * this function initializes the controller and view
     * if the resources are null, it sets them to zero
     * Calculates the resources available to one player and displays them
     */
    @FXML
    public void initialize() {
        super.initialize();
        researchbtn.setDisable(false);

        if (moneySpent == null) {
            moneySpent = 0;
        }
        if (foodSpent == null) {
            foodSpent = 0;
        }

        moenySpentTurn.setText(Integer.toString(moneySpent));
        foodSpentTurn.setText(Integer.toString(foodSpent));

        Integer technologyLevelStr = (game.getPlayerByName(client.getColor()).getCurrTechnologyLevel());
        SoldierLevel technologyEnumStr = SoldierLevel.values()[technologyLevelStr];
        currTechnologyLevel.setText(technologyEnumStr.toString());
        // colorTerritoriesbyOwner();
        showTerritoryColors(true);
        gameID.setText("gameID: " + client.getCurrentGameID().toString());
        playerNametxt.setText("Name: " + client.getColor());
        if (attackOrders == null) {
            attackOrders = new ArrayList<>();
        }
        if (moveOrders == null) {
            moveOrders = new ArrayList<>();
        }
        if (upgradeOrders == null) {
            upgradeOrders = new ArrayList<>();
        }

        calculator = new ResourceConsumeCalculator();
        Integer techAmntInt = game.getPlayerByName(client.getColor())
                .getResourceCount(new Resource(ResourceType.TECHNOLOGY));
        techAmnt.setText(techAmntInt.toString());
        Integer foodAmntInt = game.getPlayerByName(client.getColor())
                .getResourceCount(new Resource(ResourceType.FOOD));
        foodAmnt.setText(foodAmntInt.toString());

        setupRefreshTimeline();
        getOtherPlayers();
        announceAlliance();
        displayWeather();
        displayEvent();
    }

    /**
     * @param game sets the game for the controller
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * the action of moving button
     * 
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

        // if player has ally put destination territories as well in Move Action
        if (game.getPlayeryByName(client.getColor()).hasAlliance()) {

            Player ally = game.getPlayerByName(client.getColor()).getAlliancePlayer();
            List<Territory> allyTerritories = game.getPlayer(ally).getTerritories();
            ArrayList<String> allianceTerritoriesStr = mapGoBackController.setList(allyTerritories);

            ownTerritoriesStr.addAll(allianceTerritoriesStr);

        }

        // App.getPrimaryStage().setScene(scene);
        mapGoBackController.refresh(ownTerritoriesStr, ownTerritoriesStr, client.getColor(), client.getColor());
        System.out.println(ownTerritoriesStr);
        mapGoBackController.unitsComboBoxTarget.setVisible(false);
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
        mapGoBackController.unitsComboBoxTarget.setVisible(false);
        App.getPrimaryStage().setScene(scene);
    }

    /**
     * The action of pressing the research button
     */
    public void onResearchAction() {

        Player player = game.getPlayerByName(client.getColor());
        ResearchOrder researchOrdernew = new ResearchOrder(player, game);
        Integer researchCost = ResourceConsumeCalculator.computeTechConsumeForResearch(researchOrdernew);
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to upgrade? It will cost you " + researchCost + " technology resources");
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);

        // Show the confirmation dialog and wait for the user's response
        ButtonType confirmButton = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // Check if the user clicked the "OK" button
        if (confirmButton == ButtonType.OK) {
            System.out.println("research order: [" + researchOrder + "]");
            researchOrder = researchOrdernew;
            researchbtn.setDisable(true);
            System.out.println("research order: [" + researchOrder + "]");
            moneySpent += researchCost;
            moenySpentTurn.setText(Integer.toString(moneySpent));

        }
    }

    /**
     * upgrading the units through the button
     * 
     * @throws IOException
     */
    public void onUpgradeAction() throws IOException {
        URL xmlResource = getClass().getResource("/upgrade-units.fxml");
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
        mapGoBackController.destTerritorybtn.setVisible(false);

        App.getPrimaryStage().setScene(scene);

    }

    /**
     * submits the actions in this turn
     * 
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void onDone() throws ClassNotFoundException, IOException {
        System.out.println("the number of upgrade orders is: " + upgradeOrders.size());
        Action emptyAction = new Action(attackOrders, moveOrders, researchOrder, upgradeOrders, null);

        System.out.println("Current game ID before sending the orders: " + client.getCurrentGameID());
        String ActionResults = client.sendOrder(client.getCurrentGameID(), emptyAction);
        System.out.println("Action results:" + ActionResults);
        turnFeedback.setText("Action results: " + ActionResults);

        if (!ActionResults.equals("Order succeeded")) {
            attackOrders = new ArrayList<>();
            moveOrders = new ArrayList<>();
            upgradeOrders = new ArrayList<>();
            researchOrder = null;

        }

        else {

            game = client.updatedGameAfterTurn();
            moneySpent = 0;
            foodSpent = 0;
            String winResult = client.checkWin();
            if (!winResult.equals("No winner")) {
                showPopupAndExit(winResult);
            }

            if (client.checkLost().equals("You lost")) {
                checkLostPopUp(App.getPrimaryStage());
            }
            // initialize();
            // MapChooseActionController actionController = (MapChooseActionController) App
            // .loadController("submit-actions");
            setGame(game);

            this.initialize();
            App.loadScenefromMain("submit-actions");
            attackOrders = new ArrayList<>();
            moveOrders = new ArrayList<>();
            upgradeOrders = new ArrayList<>();
            researchOrder = null;
        }

    }

    private void showPopupAndExit(String message) throws ClassNotFoundException, IOException {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText(null);
        alert.setContentText(message);

        MultipleGamesController multipleGamesController = (MultipleGamesController) App
                .loadController("multiple-games");
        multipleGamesController.refresh();
        System.out.println("this is printing from the gobackcontroller");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> App.loadScenefromMain("multiple-games"));
    }

    private void checkLostPopUp(Window window) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(window);
        popup.setTitle("Game Over");

        Label label = new Label("You've lost the game. What would you like to do?");
        Button keepExpectatingBtn = new Button("Keep Expectating");
        keepExpectatingBtn.setOnAction(e -> {
            popup.close();
            try {
                onDone();
            } catch (ClassNotFoundException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } // Call the "Done" button functionality
        });

        Button logOutBtn = new Button("Go back to Main Page");
        logOutBtn.setOnAction(e -> {
            // Implement the logout functionality here

            MultipleGamesController multipleGamesController = (MultipleGamesController) App
                    .loadController("multiple-games");
            try {
                multipleGamesController.refresh();
            } catch (ClassNotFoundException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // System.out.println("this is printing from the gobackcontroller");
            App.loadScenefromMain("multiple-games");
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, keepExpectatingBtn, logOutBtn);

        Scene scene = new Scene(layout, 400, 200);
        popup.setScene(scene);
        popup.showAndWait();

    }

    // TODO add alliance order to action
    public void onFormAlliance() {

        // once the button is clicked the String is populated and the Textfield
        // in viewis reset to empy

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to try forming an Alliance with " + playersCombobox.getValue() + "?");
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(null);

        // Show the confirmation dialog and wait for the user's response
        ButtonType confirmButton = confirmationDialog.showAndWait().orElse(ButtonType.CANCEL);

        // Check if the user clicked the "OK" button
        if (confirmButton == ButtonType.OK) {

            Player ally = game.getPlayerByName(playersCombobox.getValue());

            AllianceOrder allianceOrder = new AllianceOrder(game.getPlayerByName(client.getColor()), ally);
            formAlliance.setDisable(true);
            playersCombobox.disableProperty();

        }

        // String allianceInput = allianceTextBox.getText();
        // allianceTextBox.setText("");

        // do implementation here

    }

    private Scene createChatScene() {
        ObservableList<String> messagesDisplay = client.getMessages();
        messageListView = new ListView<>(messagesDisplay);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Enter message");
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String message = messageInput.getText();
            if (!message.isBlank()) {
                client.sendMessage(client.getCurrentGameID(), client.getColor(), message);
                System.out.println(client.getMessages());
                messageInput.clear();

                // Refresh the scene
                secondStage.setScene(createChatScene());
            }
        });

        VBox secondStageLayout = new VBox(10);
        secondStageLayout.getChildren().addAll(messageListView, messageInput, submitButton);

        return new Scene(secondStageLayout, 300, 200);
    }

    public void onChat() {
        secondStage = new Stage();
        secondStage.initModality(Modality.APPLICATION_MODAL);
        secondStage.initOwner(App.getPrimaryStage());
        secondStage.setTitle("Message Viewer");

        secondStage.setScene(createChatScene());

        secondStage.show();
    }

    private void setupRefreshTimeline() {
        Timeline refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (messageListView != null && client != null) {
                ObservableList<String> messagesDisplay = client.getMessages();
                messageListView.setItems(null);
                messageListView.setItems(messagesDisplay);
            }
        }));

        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    public void getOtherPlayers() {

        allowAlliance();

        ArrayList<String> otherPlayers = new ArrayList<>();

        for (Player player : game.getPlayers()) {

            if (player.getName().equals(client.getColor())) {
                continue;
            }
            otherPlayers.add(player.getName());

        }
        ObservableList<String> sourceOptions = FXCollections.observableArrayList(otherPlayers);

        playersCombobox.setItems(sourceOptions);
    }

    public void allowAlliance() {

        if (game.getTotalPlayerNum() == 2) {

            System.out.println("total number of players is 2");
            formAlliance.setDisable(true);
        }

        if (game.checkPlayerAlliance(game.getPlayerByName(client.getColor()))) {

            System.out.println("player " + client.getColor() + "has an alliance");
            formAlliance.setDisable(true);
        }
    }

    public void announceAlliance() {

        String announcement = game.checkAllAlliance();

        if (announcement != null) {

            allianceMessageText.setText(announcement);
        }

        else {
            allianceMessageText.setText("");
        }
    }

    public void displayWeather() {

        if (game.getWeather() != null) {
            String weather = game.getWeather().toString();
            WeatherText.setText("Weather:" + weather);
        }

    }

    public void displayEvent() {

        if (game.getEvent() != null) {
            String event = game.getEvent().toString();
            EventText.setText("Event:" + event);

        }

        List<Territory> affectedTerritories = game.getAffectedTerritories();
        // TODO do alert pop up with the territories affected
    }
}
