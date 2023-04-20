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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.duke.ece651.team5.shared.PlayerConnection;
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

}