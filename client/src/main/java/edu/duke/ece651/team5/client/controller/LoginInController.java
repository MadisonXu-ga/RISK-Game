package edu.duke.ece651.team5.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginInController {

    @FXML
    public TextField userName;
    @FXML
    public TextField password;

    @FXML
    public Text loginFeedback;

    private Client client;

    @FXML
    SignUpController signUpController;

    public LoginInController(Client client) {

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
        String message = client.login(userAndPassword);
        if (message.equals("Login succeeded")) {
            createMultipleGamesPage(userAndPassword);
            // multipleGamesController.initialize();

        } else {
            loginFeedback.setText(message);
        }
    }

    private void createMultipleGamesPage(ArrayList<String> userAndPassword)
            throws IOException, ClassNotFoundException {
        System.out.println(client.login(userAndPassword));
        URL xmlResource = getClass().getResource("/multiple-games.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        MultipleGamesController multipleGamesController = new MultipleGamesController(client);
        HashMap<Class<?>, Object> controllers = new HashMap<>();

        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("multiple-games", scene);

        App.getPrimaryStage().setScene(scene);
    }

    public void onSignUpButton(ActionEvent ae) throws IOException {
        System.out.println("sign up was pressed");

        URL xmlResource = getClass().getResource("/sign-up-page.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(SignUpController.class, new SignUpController(client));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();
        Scene scene = new Scene(new StackPane(bp));

        App.getPrimaryStage().setScene(scene);

    }

}
