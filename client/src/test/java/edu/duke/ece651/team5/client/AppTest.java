package edu.duke.ece651.team5.client;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.client.App;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import org.junit.jupiter.api.Disabled;

class AppTest {


  // @Test
  // void testMain() throws IOException, ClassNotFoundException, InterruptedException{
	// 	App.main(null);
  // }

    @Test
    public void testMain() throws Exception {
        // Arrange
        String inputString = "Test Input String\n";
        System.setIn(new ByteArrayInputStream(inputString.getBytes()));
        BufferedReader mockInput = mock(BufferedReader.class);
        when(mockInput.readLine()).thenReturn("Test Input String");
        PrintStream mockOutput = mock(PrintStream.class);
        Client mockClient = mock(Client.class);


        // Act
        App.main(null);

        // Assert
        //verifyNew(Client.class).withArguments(mockInput, System.out);
        verify(mockClient).play();
    }

}
