package edu.duke.ece651.team5.server;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.server.App;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void test_GetMessage() {
      App a = new App();
      assertEquals("Hello from the server for team5", a.getMessage());
    }
}

