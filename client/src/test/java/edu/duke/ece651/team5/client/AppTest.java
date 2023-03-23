package edu.duke.ece651.team5.client;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.client.App;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.*;

import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

import org.junit.jupiter.api.Disabled;

class AppTest {

//   @Test

//  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
//   void test_main() throws IOException {
//     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//     PrintStream out = new PrintStream(bytes, true);
//     InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
//     assertNotNull(input);

//     InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
//     assertNotNull(expectedStream);

//     InputStream oldIn = System.in;
//     PrintStream oldOut = System.out;

//     try {
//       System.setIn(input);
//       System.setOut(out);
//       App.main(new String[0]);
//     } finally {
//       System.setIn(oldIn);
//       System.setOut(oldOut);
//     }

//     String expected = new String(expectedStream.readAllBytes());
//     String actual = bytes.toString();
//     assertEquals(expected, actual);
//   }
  

}
