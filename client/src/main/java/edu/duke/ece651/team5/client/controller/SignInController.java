package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;

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

public class SignInController {

    @FXML
    public TextField userName;
    @FXML
    public TextField password;

    public void onSignInButton(ActionEvent ae) {
        System.out.println("sign in was pressed");
        String user_name = userName.getText().trim();
        String user_password = password.getText().trim();

        userName.setText("");
        password.setText("");

        System.out.println("userx`: " + user_name + " Password: " + user_password);

    }

    public void onSignUpButton(ActionEvent ae) throws IOException {
        System.out.println("sign up was pressed");

        Object source = ae.getSource();

        if (source instanceof Button) {
            URL xmlResource = getClass().getResource("/sign-up-page.fxml");
            FXMLLoader loader = new FXMLLoader(xmlResource);
            BorderPane bp = loader.load();

            Button signUpBtn = (Button) source;
            Stage signUpWindow = (Stage) signUpBtn.getScene().getWindow();
            Scene scene = new Scene(new StackPane(bp));

            signUpWindow.setScene(scene);
            ;
        }

    }

}
