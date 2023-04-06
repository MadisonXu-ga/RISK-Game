package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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

public class SignInController {

    @FXML
    public TextField userName;
    @FXML
    public TextField password;

    private Client client;

    @FXML
    SignUpController signUpController;

    // public void initialize(URL location, ResourceBundle resources) {
    // signUpController.client = client;
    // }

    public SignInController(Client client) {

        this.client = client;
    }

    public void onSignInButton(ActionEvent ae) throws ClassNotFoundException, IOException {
        System.out.println("sign in was pressed");
        String user_name = userName.getText().trim();
        String user_password = password.getText().trim();

        ArrayList<String> userAndPassword = new ArrayList<>();
        userAndPassword.add(user_name);
        userAndPassword.add(user_password);

        userName.setText("");
        password.setText("");

        System.out.println("userx`: " + user_name + " Password: " + user_password);
        String message = client.Login(userAndPassword);
        if (message.equals("Login succeeded")) {
            System.out.println(client.Login(userAndPassword));
            URL xmlResource = getClass().getResource("/multiple-games.fxml");
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>, Object> controllers = new HashMap<>();
            controllers.put(MultipleGamesController.class, new MultipleGamesController(client));
            loader.setControllerFactory((c) -> {
                return controllers.get(c);
            });

            BorderPane bp = loader.load();
            Object source = ae.getSource();
            Button signUpBtn = (Button) source;
            Stage signUpWindow = (Stage) signUpBtn.getScene().getWindow();
            Scene scene = new Scene(new StackPane(bp));

            signUpWindow.setScene(scene);
        }
    }

    public void onSignUpButton(ActionEvent ae) throws IOException {
        System.out.println("sign up was pressed");

        Object source = ae.getSource();

        if (source instanceof Button) {
            URL xmlResource = getClass().getResource("/sign-up-page.fxml");
            FXMLLoader loader = new FXMLLoader(xmlResource);

            HashMap<Class<?>, Object> controllers = new HashMap<>();
            controllers.put(SignUpController.class, new SignUpController(client));
            loader.setControllerFactory((c) -> {
                return controllers.get(c);
            });

            BorderPane bp = loader.load();

            Button signUpBtn = (Button) source;
            Stage signUpWindow = (Stage) signUpBtn.getScene().getWindow();
            Scene scene = new Scene(new StackPane(bp));

            signUpWindow.setScene(scene);

        }

    }

}
