package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SignUpController {

    public Client client;
    @FXML
    public TextField userName;
    @FXML
    public TextField password;

    // int clickCount = 0;

    // public void onSelect(MouseEvent ae) {
    // clickCount++;

    // Object source = ae.getSource();

    // if (source instanceof Button) {

    // Button signUpBtn = (Button) source;

    // if (clickCount % 2 != 0) {
    // // Apply first style
    // signUpBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
    // } else {
    // // Apply second style
    // signUpBtn.setStyle("");
    // }

    // }

    // }

    public SignUpController(Client client) {

        this.client = client;

    }

    public void onCreateAccount(ActionEvent ae) throws ClassNotFoundException,
            IOException {
        System.out.println("CreateAccount was pressed");
        String user_name = userName.getText().trim();
        String user_password = password.getText().trim();

        ArrayList<String> userAndPassword = new ArrayList<>();
        userAndPassword.add(user_name);
        userAndPassword.add(user_password);

        userName.setText("");
        password.setText("");

        System.out.println("userx`: " + user_name + " Password: " +
                user_password);

        String message = client.SignUp(userAndPassword);

        System.out.println("Message: [" + message + "]");
        if (message.equals("Sign up succeeded")) {
            // if (message.equals("Sign up suceeded")) {
            // System.out.println(message);
            // }
            App.loadScenefromMain("login-page");

        }
    }

}
