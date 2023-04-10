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
    @FXML
    SignUpController signUpController;

    public Client client;

    /**
     * @param client added for testability and pass a mocked client
     */
    public LoginInController(Client client) {

        this.client = client;
    }

    /**
     * once we press the sign in button we have two options:
     * 1) the client does not exist, and a text showing "Not Exists" shows
     * 2) the client does exist and the user is able to go to the next loading
     * screen
     * 
     * @param ae action of pressing the signIn button
     * @throws ClassNotFoundException
     * @throws IOException
     */
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
            // multipleGamesController.initializeButtons();

        } else {
            loginFeedback.setText(message);
        }
    }

    /**
     * @param userAndPassword passes the user and password to be sent to server and
     *                        get into the game
     * @throws IOException
     * @throws ClassNotFoundException
     *                                this function only is called if an existing
     *                                user and password are passed to server
     *                                creates the multiple-games scene and loads it
     *                                adds multiple-games to main, and sets the
     *                                scene using primaryStage
     */
    private void createMultipleGamesPage(ArrayList<String> userAndPassword)
            throws IOException, ClassNotFoundException {
        System.out.println(client.login(userAndPassword));
        URL xmlResource = getClass().getResource("/multiple-games2.fxml");
        FXMLLoader loader = new FXMLLoader(xmlResource);

        MultipleGamesController multipleGamesController = new MultipleGamesController(client);

        HashMap<Class<?>, Object> controllers = new HashMap<>();

        controllers.put(MultipleGamesController.class, multipleGamesController);
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        StackPane bp = loader.load();

        Scene scene = new Scene(new StackPane(bp));
        App.addScenetoMain("multiple-games", scene);
        App.addControllertoMain("multiple-games", multipleGamesController);
        multipleGamesController.refresh();
        App.getPrimaryStage().setScene(scene);

        // multipleGamesController.initialize();
    }

    /**
     * if the user does not have an account or needs a new one they press Signup
     * this function takes them to the sign up page
     * no need to store sign up in memory since we can always access it from login
     * 
     * @param ae
     * @throws IOException
     */
    public void onSignUpButton(ActionEvent ae) throws IOException {
        System.out.println("sign up was pressed");
        loginFeedback.setText("");

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
