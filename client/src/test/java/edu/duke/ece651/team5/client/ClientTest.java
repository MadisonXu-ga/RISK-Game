package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.PlayerConnection;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.RISKMap;

public class ClientTest {
  public static Client createPlayer(PlayerConnection test, BufferedReader input, PrintStream output)
      throws UnknownHostException, IOException {
    Client client = new Client(input, output) {
      @Override
      public void createPlayer() {
        try {
          this.playerConnection = test;
          this.textPlayer = new TextPlayer(input, output);
        } catch (Exception e) {
        }
      }
    };
    client.createPlayer();
    return client;
  }

  private Client client;
  private PlayerConnection test;

  @BeforeEach
  public void setUp() throws UnknownHostException, IOException {

    RISKMap map = mock(RISKMap.class);
    test = mock(PlayerConnection.class);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("1"));
    PrintStream output = new PrintStream(bytes, true);
    this.client = createPlayer(test, input, output);
  }

  @Test
  public void confirmGameTest() throws ClassNotFoundException, IOException {

    String expectedMessage = "Game confirmed";
    when(test.readData()).thenReturn(expectedMessage + "\n");

    String actualMessage = client.confirmGame();

    assertEquals(expectedMessage, actualMessage);
    verify(test, times(1)).readData();
  }

  @Test
  public void confirmPlayerColor() {

    client.setColor("red");
    assertEquals("red", client.getColor());

  }

  @Test
  public void confirmGameID() throws ClassNotFoundException, IOException {

    client.setGameID(1);
    assertEquals(1, client.getCurrentGameID());

  }

  @Test
  public void loginTest() throws ClassNotFoundException, IOException {

    String expectedMessage = "Login confirmed";
    when(test.readData()).thenReturn(expectedMessage + "\n");
    String userName = "user";
    String password = "password";
    ArrayList<String> userAndPassoword = new ArrayList<>();
    userAndPassoword.add(userName);
    userAndPassoword.add(password);

    String actualMessage = client.login(userAndPassoword);

    assertEquals(expectedMessage, actualMessage);
    verify(test, times(1)).readData();

  }

  @Test
  public void receiveColorTest() throws ClassNotFoundException, IOException {

    String color = "red";
    when(test.readData()).thenReturn(color);

    String colorReceived = client.receiveColor();

    assertEquals(color, colorReceived);
  }

  @Test
  public void beginNewGameTest() throws IOException, ClassNotFoundException {

    String message = "Joined Success";
    Integer playerAmount = 4;
    when(test.readData()).thenReturn(message);
    String messageReceived = client.joinNewGame(playerAmount);

    assertEquals(message, messageReceived);
  }

  @Test
  public void getJoinableGamesTest() throws IOException, ClassNotFoundException {

    ArrayList<Integer> gameIDs = new ArrayList<>();
    gameIDs.add(4);
    gameIDs.add(3);
    gameIDs.add(1);

    when(test.readData()).thenReturn(gameIDs);
    ArrayList<Integer> messageReceived = client.getJoinableGames();

    assertEquals(gameIDs, messageReceived);
  }

  @Test
  public void joinNewGameTest() throws IOException, ClassNotFoundException {

    String message = "Joined Success";
    Integer gameID = 2;
    when(test.readData()).thenReturn(message);
    String messageReceived = client.joinNewGame(gameID);

    assertEquals(message, messageReceived);
  }

  @Test
  public void getGameTest() throws IOException, ClassNotFoundException {

    Game mockedGame = mock(Game.class);

    when(test.readData()).thenReturn(mockedGame);
    Game messageReceived = client.getGame();

    assertEquals(mockedGame, messageReceived);
  }

  @Test
  public void updateGameAfterTurnTest() throws IOException, ClassNotFoundException {

    Game mockedGame = mock(Game.class);

    when(test.readData()).thenReturn(mockedGame);
    Game messageReceived = client.updatedGameAfterTurn();

    assertEquals(mockedGame, messageReceived);
  }

  @Test
  public void getGameConfirmationTest() throws IOException, ClassNotFoundException {

    String message = "Joined Success";

    when(test.readData()).thenReturn(message);
    String messageReceived = client.getGameConfirmation();

    assertEquals(message, messageReceived);
  }

  @Test
  public void getGameIDTest() throws IOException, ClassNotFoundException {

    // String message = "Joined Success";
    Integer gameID = 2;
    when(test.readData()).thenReturn(gameID);
    Integer messageReceived = client.getGameID();

    assertEquals(gameID, messageReceived);
  }

  @Test
  public void signUpTest() throws IOException, ClassNotFoundException {

    String expectedMessage = "Signup confirmed";
    when(test.readData()).thenReturn(expectedMessage + "\n");
    String userName = "user";
    String password = "password";
    ArrayList<String> userAndPassoword = new ArrayList<>();
    userAndPassoword.add(userName);
    userAndPassoword.add(password);

    String actualMessage = client.signUp(userAndPassoword);

    assertEquals(expectedMessage, actualMessage);
    verify(test, times(1)).readData();
  }

  @Test
  public void LogOutfromXTest() throws IOException {
    client.logOutfromX();

    verify(test).writeData("Log out");
    verify(test).close();
  }

  @Test
  public void sendPlacementOrderTest() throws IOException, ClassNotFoundException {

    String message = "Joined Success";
    Integer gameID = 4;
    HashMap<String, Integer> placementOrders = new HashMap<>();

    when(test.readData()).thenReturn(message);
    String messageReceived = client.sendPlacementOrder(gameID, placementOrders);

    assertEquals(message, messageReceived);
  }

  @Test
  public void recvUpdatedGameTest() throws IOException, ClassNotFoundException {

    Game mockedGame = mock(Game.class);

    when(test.readData()).thenReturn(mockedGame);
    Game messageReceived = client.recvUpdatedGame();

    assertEquals(mockedGame, messageReceived);
  }

  @Test
  public void checkWinTest() throws IOException, ClassNotFoundException {

    String message = "You won";

    when(test.readData()).thenReturn(message);
    String messageReceived = client.checkWin();

    assertEquals(message, messageReceived);
  }

  @Test
  public void checkLostTest() throws IOException, ClassNotFoundException {

    String message = "You lost";

    when(test.readData()).thenReturn(message);
    String messageReceived = client.checkLost();

    assertEquals(message, messageReceived);
  }

  @Test
  public void continueGameTest() throws IOException, ClassNotFoundException {

    String message = "You can continue";
    Integer gameToContinue = 4;

    when(test.readData()).thenReturn(message);
    String messageReceived = client.continueGame(gameToContinue);

    assertEquals(message, messageReceived);
  }

  @Test
  public void sendOrder() throws IOException, ClassNotFoundException {

    String message = "You can continue";
    Integer gameID = 4;
    Action action = new Action(null, null, null, null, null);
    when(test.readData()).thenReturn(message);
    String messageReceived = client.sendOrder(gameID, action);

    assertEquals(message, messageReceived);
  }

  @Test
  public void sendGameIDTest() throws IOException, ClassNotFoundException {

    String message = "game ID exists";
    Integer gameID = 2;
    when(test.readData()).thenReturn(message);
    String messageReceived = client.sendGameID(gameID);

    assertEquals(message, messageReceived);
  }

  @Test
  public void getGamesStartedTest() throws IOException, ClassNotFoundException {

    // String message = "game ID exists";
    ArrayList<Integer> retrievedGames = new ArrayList<>();
    when(test.readData()).thenReturn(retrievedGames);
    ArrayList<Integer> messageReceived = client.getGamesStarted();

    assertEquals(retrievedGames, messageReceived);
  }

}