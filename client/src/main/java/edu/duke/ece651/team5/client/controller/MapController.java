package edu.duke.ece651.team5.client.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.Soldier;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;

public class MapController extends GoBackController {

    @FXML
    AnchorPane rightSideScreen;

    public Client client;
    public Game game;
    protected ArrayList<Territory> ownedTerritories;

    public Button[] gameButtons;
    public HashMap<String, Button> buttonsHashmap;

    public ArrayList<String> list = new ArrayList<>(
            Arrays.asList("Narnia", "Elantris", "Gondor", "Mordor", "Hogwarts",
                    "Thalassia", "Arathia", "Eryndor", "Sylvaria", "Kaelindor", "Emberfall", "Verdantia"));

    /**
     * @param client
     */
    public MapController(Client client) {

        this.client = client;
        this.game = null;
    }

    public MapController(Client client, Game game) {

        this.client = client;
        this.game = game;
    }

    /**
     * refresh contents of the page and initializes territories
     */
    @FXML
    public void initialize() {

        if (gameButtons == null) {
            gameButtons = rightSideScreen.lookupAll(".button:@territory.css").toArray(new Button[0]);
        }

    }

    /**
     * @return see if the list has the buttons from page
     */
    public ArrayList<String> checkListAgainstGameButtons() {
        boolean allFound = true;
        ArrayList<String> foundTerri = new ArrayList<>();
        for (String element : list) {
            boolean found = false;
            for (Button button : gameButtons) {
                if (button.getId().equals(element)) {
                    found = true;
                    foundTerri.add(element);
                    break;
                }
            }
            if (!found) {
                allFound = false;
                return (new ArrayList<>());
            }
        }
        return foundTerri;
    }

    /**
     * @param element see the name of button we need to return (String )
     * @return
     */
    public Button getMatchingGameButton(String element) {
        for (Button button : gameButtons) {
            if (button.getId().equals(element)) {
                return button;
            }
        }
        return null;
    }

    /**
     * @param button to be disabled (and not clickable). It will be shown as grey
     */
    public void disableButton(Button button) {
        button.setDisable(true);

    }

    /**
     * you can call this function to show the territory the same color
     * as the player that owns it
     * 
     * @param button
     */
    public void assignButtonToPlayer(Button button, String color) {

        String getStyle = color + "Playerbtn";
        button.getStyleClass().add(getStyle);
    }

    public String getTerritoryTroops(String terri) {

        // System.out.print("the territory you are trying to find is: [" + terri + "]");
        if (game != null) {

            Map<Soldier, Integer> soldiersObject = game.getMap().getTerritoryByName(terri).getSoldierArmy()
                    .getAvailableSoldiers();

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Soldier, Integer> entry : soldiersObject.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
            }

            if (game.getMap().getTerritoryByName(terri).getOwner().hasAlliance()) {

                String allyName = game.getMap().getTerritoryByName(terri).getOwner().getAlliancePlayer().getName();
                sb.append("ally: " + allyName).append(System.lineSeparator());
                sb.append("Units:").append(System.lineSeparator());
                Map<Soldier, Integer> allySoldiersObject = game.getMap().getTerritoryByName(terri)
                        .getAllianceSoliderArmy()
                        .getAvailableSoldiers();

                for (Map.Entry<Soldier, Integer> entry : allySoldiersObject.entrySet()) {
                    sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
                }

            }

            return sb.toString();
        }
        return null;
    }

    public void handleMouseEntered(MouseEvent event) {
        Button btn = (Button) event.getSource();
        Popup popup = new Popup();
        // popup.setAutoHide(true);
        Pane popupContent = new Pane();
        popupContent.setStyle("-fx-background-color: #FFFFFF;");

        // Add some content to the popup's pane
        LocalTime time = LocalTime.now();
        Label label = new Label(game.getMap().getTerritoryByName(btn.getId()).getOwner().getName() + "\n" + btn.getId()
                + "\n\n" + getTerritoryTroops(btn.getId()));
        label.setFont(Font.font(18));
        label.setLayoutX(0);
        label.setLayoutY(0);
        popupContent.getChildren().add(label);

        // Set the content of the popup window to the pane
        popup.getContent().add(popupContent);
        Bounds boundsInScene = btn.localToScene(btn.getBoundsInLocal());
        double x = btn.localToScene(btn.getBoundsInLocal()).getMinX();
        double y = btn.localToScene(btn.getBoundsInLocal()).getMinY();
        popup.show(btn.getScene().getWindow(), x - 50, y);

        btn.setOnMouseExited(mouseEvent -> popup.hide());
    }

    /**
     * shows your own territories only
     */
    protected void colorTerritoriesbyOwner() {
        showTerritoryColors(true);
    }

    /**
     * shows the colors of what territories are being shown in the controller or
     * grayed if not interested
     * 
     * @param showAll
     */
    protected void showTerritoryColors(boolean showAll) {

        if (game != null) {

            System.out.println("size of game buttons in the pane: " + gameButtons.length);

            System.out.println("This is coloring the territories now");
            for (Player playerx : game.getPlayers()) {

                if (showAll == false && !(client.getColor().equals(playerx.getName()))) {

                    continue;
                }
                for (Territory territory : playerx.getTerritories()) {
                    System.out.println(
                            "therritory " + territory.getName() + "is owned by " + territory.getOwner().getName());

                    Button matchingButton = getMatchingGameButton(territory.getName());
                    System.out.println("matching button ID is [" + matchingButton.getId() + "]");
                    if (matchingButton != null) {
                        // System.out.println("color: [" + client.getColor() + "]");
                        assignButtonToPlayer(matchingButton, playerx.getName());

                        matchingButton.getStyleClass().clear();
                        matchingButton.getStyleClass().add(territory.getName() + "btn");

                        matchingButton.getStyleClass()
                                .add(territory.getOwner().getName()
                                        + "Playerbtn");

                        // matchingButton.setStyle("-fx-background-color: red;");
                    }
                }
            }
        }
    }

    /**
     * only colors your territories
     */
    protected void colorTerritoriesYouOwn() {
        ownedTerritories = new ArrayList<>();
        for (Territory territory : game.getPlayerByName(client.getColor()).getTerritories()) {
            ownedTerritories.add(territory);
            Button matchingButton = getMatchingGameButton(territory.getName());
            if (matchingButton != null) {
                assignButtonToPlayer(matchingButton, client.getColor());
            }

        }
    }

}
