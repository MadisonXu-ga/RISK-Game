package edu.duke.ece651.team5.client.controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class SignUpController {

    int clickCount = 0;

    public void onSelect(MouseEvent ae) {
        clickCount++;

        Object source = ae.getSource();

        if (source instanceof Button) {

            Button signUpBtn = (Button) source;

            if (clickCount % 2 != 0) {
                // Apply first style
                signUpBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
            } else {
                // Apply second style
                signUpBtn.setStyle("");
            }

        }

    }
}
