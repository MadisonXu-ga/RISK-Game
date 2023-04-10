package edu.duke.ece651.team5.client.controllerTests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.matcher.control.TextMatchers;

import edu.duke.ece651.team5.client.App;
import edu.duke.ece651.team5.client.Client;
import edu.duke.ece651.team5.client.controller.MultipleGamesController;
import edu.duke.ece651.team5.client.controller.LoginInController;
import edu.duke.ece651.team5.client.controller.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class SignupControllerTest {
    App a;
    Stage stage;
    Client client;

    @Start
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        this.client = Mockito.mock(Client.class);
        a = new App(client);
        // a = new App();
        a.start(stage);

        URL xmlResource = getClass().getResource("/sign-up-page.fxml");

        FXMLLoader loader = new FXMLLoader(xmlResource);

        HashMap<Class<?>, Object> controllers = new HashMap<>();
        controllers.put(LoginInController.class, new LoginInController(client));
        controllers.put(SignUpController.class, new SignUpController(client));
        loader.setControllerFactory((c) -> {
            return controllers.get(c);
        });

        BorderPane bp = loader.load();

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Calculate the desired width and height as a percentage of the screen size
        double sceneWidth = screenWidth * 0.9;
        double sceneHeight = screenHeight * 0.9;

        System.out.println("w:" + sceneWidth + ", H: " + sceneHeight);

        Scene scene = new Scene(new StackPane(bp), sceneWidth, sceneHeight);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    @Test
    public void emptyfnv() {

    }

    // @Test
    // void testSignUpBtn(FxRobot robot) throws ClassNotFoundException, IOException,
    // TimeoutException {

    // when(client.signUp(any(ArrayList.class))).thenReturn("User exists");
    // FxAssert.verifyThat("#createAccount", NodeMatchers.isVisible());
    // FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    // String userNameStr = "coolUserName";
    // String password = "intelligentPassword";
    // robot.clickOn("#userName").write(userNameStr);
    // robot.clickOn("#password").write(password);
    // robot.clickOn("#createAccount");
    // FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#signUpFeedback", TextMatchers.hasText("User exists"));
    // // signUpFeedback

    // }

    // @Test
    // void testSignUpBtn_success(FxRobot robot) throws ClassNotFoundException,
    // IOException, TimeoutException {

    // when(client.signUp(any(ArrayList.class))).thenReturn("Sign up succeeded");
    // FxAssert.verifyThat("#createAccount", NodeMatchers.isVisible());
    // FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    // String userNameStr = "coolUserName";
    // String password = "intelligentPassword";
    // robot.clickOn("#userName").write(userNameStr);
    // robot.clickOn("#password").write(password);
    // robot.clickOn("#createAccount");
    // FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    // FxAssert.verifyThat("#loginBtn", NodeMatchers.isVisible());
    // FxAssert.verifyThat("#signupBtn", NodeMatchers.isVisible());
    // // signUpFeedback

    // }
}
