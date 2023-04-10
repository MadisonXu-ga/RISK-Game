package edu.duke.ece651.team5.client.controllerTests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class SigninControllerTest {
    App a;
    Stage stage;
    Client client;

    @Start
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        this.client = Mockito.mock(Client.class);
        a = new App(client);
        a.start(stage);
        this.stage = stage;

    }

    @Test
    public void srtfnv() {

    }

    @Test
    void testSignInButton(FxRobot robot) throws ClassNotFoundException,
    IOException {

    when(client.login(any(ArrayList.class))).thenReturn("Not exists");
    FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#loginFeedback", TextMatchers.hasText(""));

    String userNameStr = "coolUserName";
    String password = "intelligentPassword";
    robot.clickOn("#userName").write(userNameStr);
    robot.clickOn("#password").write(password);
    FxAssert.verifyThat("#userName",
    TextInputControlMatchers.hasText(userNameStr));
    FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(password));
    robot.clickOn("#loginBtn");
    FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#loginFeedback", TextMatchers.hasText("Not exists"));

    }

    @Test
    void testSignInButton_success(FxRobot robot) throws ClassNotFoundException,
    IOException {

    when(client.login(any(ArrayList.class))).thenReturn("Login succeeded");
    FxAssert.verifyThat("#userName", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(""));
    FxAssert.verifyThat("#loginFeedback", TextMatchers.hasText(""));

    String userNameStr = "coolUserName";
    String password = "intelligentPassword";
    robot.clickOn("#userName").write(userNameStr);
    robot.clickOn("#password").write(password);
    FxAssert.verifyThat("#userName",
    TextInputControlMatchers.hasText(userNameStr));
    FxAssert.verifyThat("#password", TextInputControlMatchers.hasText(password));
    robot.clickOn("#loginBtn");
    FxAssert.verifyThat("#beginNewGamebtn", NodeMatchers.isVisible());
    FxAssert.verifyThat("#joinOtherGamesbtn", NodeMatchers.isVisible());

    MultipleGamesController multipleGamesController = (MultipleGamesController) App
                        .loadController("multiple-games");
                multipleGamesController.refresh();
    Button[] buttons = multipleGamesController.getButtons();
    robot.clickOn("#beginNewGamebtn");
    buttons = multipleGamesController.getButtons();
    robot.clickOn("#saveAndExit");
    buttons = multipleGamesController.getButtons();
    }

    @Test
    void testSignUpBtn(FxRobot robot) throws ClassNotFoundException, IOException,
            TimeoutException {

        robot.clickOn("#signupBtn");
        FxAssert.verifyThat("#createAccount", NodeMatchers.isVisible());

    }
}
