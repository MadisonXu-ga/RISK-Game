package edu.duke.ece651.team5.client.controller;

import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Popup;

public class MapGoBackController extends MapController {

    @FXML
    Button goBackbtn;

    public void handleMouseEntered(MouseEvent event) {
        Button btn = (Button) event.getSource();
        Popup popup = new Popup();
        // popup.setAutoHide(true);
        Pane popupContent = new Pane();
        popupContent.setStyle("-fx-background-color: #FFFFFF;");

        // Add some content to the popup's pane
        LocalTime time = LocalTime.now();
        Label label = new Label(btn.getId() + "\n\n" + time);
        label.setFont(Font.font(18));
        label.setLayoutX(0);
        label.setLayoutY(0);
        popupContent.getChildren().add(label);

        // Set the content of the popup window to the pane
        popup.getContent().add(popupContent);

        // popup.show(btn.getScene().getWindow(), event.getScreenX(),
        // event.getScreenY());
        Bounds boundsInScene = btn.localToScene(btn.getBoundsInLocal());
        double x = boundsInScene.getMinX() + boundsInScene.getWidth();
        double y = boundsInScene.getMinY() + boundsInScene.getHeight() / 2;
        popup.show(btn.getScene().getWindow(), x, y);
        btn.setOnMouseExited(mouseEvent -> popup.hide());
    }
}
