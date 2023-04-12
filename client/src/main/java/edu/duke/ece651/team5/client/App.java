/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team5.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import edu.duke.ece651.team5.client.controller.MultipleGamesController;
import edu.duke.ece651.team5.client.controller.GoBackController;
import edu.duke.ece651.team5.client.controller.LoginInController;
import edu.duke.ece651.team5.client.controller.SignUpController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

// public class App {
//   public static void main(String[] args) {
//     BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//     Client client = new Client(input, System.out);
//     client.play();
//   }
// }
public class App extends Application {

  public static Scene signInScene;
  public static Stage primaryStage;
  public static HashMap<String, Scene> loadedScenes;
  public static HashMap<String, Object> loadedControllers;

  public Client classClient;

  // include to make the code testable by passing a mock client
  public App(Client client) {
    this.classClient = client;
  }

  // regular loading of the page
  public App() throws UnknownHostException, IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    this.classClient = new Client(input, System.out);
  }

  @Override
  public void start(Stage stage) throws IOException {

    // this will be used to save important scenes and main stage thorughout the game
    loadedScenes = new HashMap<>();
    loadedControllers = new HashMap<>();
    primaryStage = stage;

    // get rid of this later
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");

    // load our login page
    URL xmlResource = getClass().getResource("/login-page.fxml");
    FXMLLoader loader = new FXMLLoader(xmlResource);
    HashMap<Class<?>, Object> controllers = new HashMap<>();
    controllers.put(LoginInController.class, new LoginInController(classClient));
    controllers.put(SignUpController.class, new SignUpController(classClient));
    controllers.put(GoBackController.class, new GoBackController());
    controllers.put(MultipleGamesController.class, new MultipleGamesController(classClient));
    loader.setControllerFactory((c) -> {
      return controllers.get(c);
    });

    BorderPane bp = loader.load();

    // set our game to be 90% of our screen size
    double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

    // Calculate the desired width and height as a percentage of the screen size
    double sceneWidth = screenWidth * 0.9;
    double sceneHeight = screenHeight * 0.9;

    // included for testing purposes; erase later
    System.out.println("w:" + sceneWidth + ", H: " + sceneHeight);

    // create scene for the game and load the login page
    Scene scene = new Scene(new StackPane(bp), sceneWidth, sceneHeight);
    signInScene = scene;
    addScenetoMain("login-page", scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

    primaryStage.setOnCloseRequest(event -> {

      event.consume();
      logoutGame(getPrimaryStage());
      try {
        classClient.logOutfromX();
      } catch (IOException e) {

        e.printStackTrace();
      }
    });
  }

  // this function will be used from the controllers to set new scenes in the same
  // stage
  /**
   * @return primaryStage as a Stage
   */
  public static Stage getPrimaryStage() {
    return primaryStage;
  }

  // add the scenes that we want to keep coming back to here
  /**
   * @param sceneName String name of the scene
   * @param scene     that we want to add
   */
  public static void addScenetoMain(String sceneName, Scene scene) {

    loadedScenes.put(sceneName, scene);
  }

  public static void addControllertoMain(String controllerName, Object controller) {

    loadedControllers.put(controllerName, controller);
  }

  // load the screens by passing their names in string format
  /**
   * @param sceneName in string format
   */
  public static void loadScenefromMain(String sceneName) {

    Scene loadedScene = loadedScenes.get(sceneName);
    primaryStage.setScene(loadedScene);
    primaryStage.show();

  }

  public static Object loadController(String sceneName) {

    Object loadedContoller = loadedControllers.get(sceneName);
    return loadedContoller;
  }

  public static void logoutGame(Stage stage) {

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("You are about to logout!");
    alert.setContentText("Do you want to save before exiting?");

    if (alert.showAndWait().get() == ButtonType.OK) {

      primaryStage.close();

    }
  }

  public static void main(String[] args) {

    launch();
  }

}
